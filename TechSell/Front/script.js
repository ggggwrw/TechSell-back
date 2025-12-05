// ===== Validación de formulario contacto ====
const formulario = document.getElementById('form-contacto');
if (formulario) {
  formulario.addEventListener('submit', function (e) {
    e.preventDefault();

    const nombre = formulario.nombre;
    const email = formulario.email;
    const mensaje = formulario.mensaje;

    let valido = true;
    let errores = [];

    if (nombre.value.trim() === "") {
      errores.push("El nombre es obligatorio.");
      valido = false;
    }

    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!emailRegex.test(email.value.trim())) {
      errores.push("El correo no tiene un formato válido.");
      valido = false;
    }

    if (mensaje.value.trim().length < 10) {
      errores.push("El mensaje debe tener al menos 10 caracteres.");
      valido = false;
    }

    if (!valido) {
      alert("Error:\n" + errores.join("\n"));
      return;
    }

    alert("Gracias por contactarnos. Te responderemos a la brevedad.");
    formulario.reset();
  });
}

// ===== Configuración Backend =====
// Ajusta al puerto activo del backend; dev usa 8081
const API_BASE = 'http://localhost:8081/api';

async function apiGet(path) {
  const res = await fetch(`${API_BASE}${path}`);
  if (!res.ok) throw new Error(`Error GET ${path}: ${res.status}`);
  return res.json();
}

async function apiPost(path, body) {
  const res = await fetch(`${API_BASE}${path}`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: body ? JSON.stringify(body) : null
  });
  if (!res.ok) throw new Error(`Error POST ${path}: ${res.status}`);
  return res.json();
}

async function apiPut(path, body) {
  const res = await fetch(`${API_BASE}${path}`, {
    method: 'PUT',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(body)
  });
  if (!res.ok) throw new Error(`Error PUT ${path}: ${res.status}`);
  return res.json();
}

// ===== Cargar productos desde Backend + manuales =====
const contenedor = document.getElementById('contenedor-productos');

if (contenedor) {
  (async () => {
    try {
      const backendProductos = await apiGet('/productos');
      // Mapear a formato del front (title, price, image)
      const mapped = backendProductos.map(p => ({
        id: p.id,
        title: p.nombre || p.title || `Producto ${p.id}`,
        price: p.precio ?? p.price ?? 0,
        image: p.image || 'ImgVarias/imgaen ayuda.jfif'
      }));
      const productos = [...mapped, ...(window.productosManual || [])];

      productos.forEach(producto => {
        const card = document.createElement('div');
        card.className = 'producto';
        card.innerHTML = `
          <div class="producto-img-container">
            <img src="${producto.image}" alt="${producto.title}">
            <button class="btn-carrito" onclick="agregarAlCarrito(${producto.id})">Agregar al carrito</button>
          </div>
          <h3>${producto.title}</h3>
          <p>$${producto.price}</p>
        `;
        contenedor.appendChild(card);
      });
    } catch (err) {
      console.error(err);
    }
  })();
}

// ===== Carrito Dinámico ====
let carrito = JSON.parse(localStorage.getItem('carrito')) || [];

function actualizarContadorCarrito() {
  const total = carrito.reduce((acc, prod) => acc + prod.cantidad, 0);
  localStorage.setItem('carrito', JSON.stringify(carrito));
  const icono = document.querySelector('a[href="carrito.html"]');
  if (icono) icono.innerText = `🛒 (${total})`;
}

async function agregarAlCarrito(id) {
  const productoManual = (window.productosManual || []).find(p => p.id === id);
  if (productoManual) {
    const existe = carrito.find(item => item.id === productoManual.id);
    if (existe) {
      existe.cantidad++;
    } else {
      carrito.push({ ...productoManual, cantidad: 1 });
    }
    actualizarContadorCarrito();
    return;
  }
  // Producto desde backend
  try {
    const p = await apiGet(`/productos/${id}`);
    const mapped = {
      id: p.id,
      title: p.nombre || `Producto ${p.id}`,
      price: p.precio ?? 0,
      image: p.image || 'ImgVarias/imgaen ayuda.jfif'
    };
    const existe = carrito.find(item => item.id === mapped.id);
    if (existe) {
      existe.cantidad++;
    } else {
      carrito.push({ ...mapped, cantidad: 1 });
    }
    actualizarContadorCarrito();
  } catch (err) {
    console.error(err);
  }
}

function mostrarCarrito() {
  const carritoSection = document.querySelector('section');
  if (!carritoSection) return;

  carritoSection.style.minHeight = "60vh";

  if (carrito.length === 0) {
    carritoSection.innerHTML = "<h2>Carrito de Compras</h2><p>Tu carrito está vacío.</p>";
    return;
  }

  let html = `<h2>Carrito de Compras</h2><div class="grid-productos grid-centrado">`;
  let total = 0;

  carrito.forEach((prod, i) => {
    total += prod.price * prod.cantidad;
    html += `
      <div class="producto">
        <div class="contenido-info">
          <img src="${prod.image}" alt="${prod.title}" style="width:100px; height:100px; margin: 0 auto;">
          <h3>${prod.title}</h3>
          <p>Precio: $${prod.price}</p>
          <p>Cantidad: <input type="number" min="1" value="${prod.cantidad}" onchange="cambiarCantidad(${i}, this.value)"></p>
          <p>Total: $${(prod.price * prod.cantidad).toFixed(2)}</p>
        </div>
        <button onclick="eliminarProducto(${i})">Eliminar</button>
      </div>`;
  });

html += `</div>
  <div class="total-carrito">
    <p>Total de la compra: $${total.toFixed(2)}</p>
    <div class="botones-carrito">
      <button onclick="cancelarCarrito()">Cancelar</button>
      <button onclick="procesarPedidoBackend()">Comprar</button>
    </div>
  </div>`;
  carritoSection.innerHTML = html;
}

function cambiarCantidad(index, cantidad) {
  carrito[index].cantidad = parseInt(cantidad);
  actualizarContadorCarrito();
  mostrarCarrito();
}

function eliminarProducto(index) {
  carrito.splice(index, 1);
  actualizarContadorCarrito();
  mostrarCarrito();
}

function cancelarCarrito() {
  carrito = [];
  actualizarContadorCarrito();
  mostrarCarrito();
}

if (window.location.pathname.includes('carrito.html')) {
  mostrarCarrito();
}

actualizarContadorCarrito();

// ===== Integración con Pedidos del Backend =====
async function procesarPedidoBackend() {
  if (carrito.length === 0) return alert('Tu carrito está vacío');
  try {
    const pedido = await apiPost('/pedidos');
    for (const item of carrito) {
      const cantidad = item.cantidad || 1;
      await fetch(`${API_BASE}/pedidos/${pedido.id}/agregar/${item.id}?cantidad=${cantidad}`, { method: 'POST' });
    }
    const confirmado = await apiPost(`/pedidos/${pedido.id}/confirmar`);
    alert(`Pedido #${confirmado.id} confirmado. Total: $${(confirmado.total || 0).toFixed(2)}`);
    cancelarCarrito();
  } catch (err) {
    alert('Error procesando el pedido: ' + err.message);
    console.error(err);
  }
}
