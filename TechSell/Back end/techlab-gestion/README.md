# Techlab Gestión Backend

## Requisitos
- Java 17+
- Maven 3.8+
- MySQL 8.x

## Configuración
- Crea la base `techlab_db` en MySQL.
- Define credenciales vía variables de entorno:
	- `DB_USER` (por defecto `root`)
	- `DB_PASS` (por defecto `password`)
- Archivo `application.yml` usa MySQL en puerto `8080` y carga `schema.sql` y `data.sql`.
- Perfil `dev` (`application-dev.yml`) usa H2 en `8081`.

## Inicialización de schema y datos
- `src/main/resources/schema.sql` crea tablas `producto`, `pedido`, `linea_pedido`.
- `src/main/resources/data.sql` inserta productos iniciales.

## Ejecutar
```powershell
cd "c:\Users\nicol\OneDrive\Desktop\pana\Back end\techlab-gestion"
mvn spring-boot:run
```

Para usar H2 (perfil dev):
```powershell
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

## Endpoints
- `GET /api/productos` listar
- `GET /api/productos/{id}` detalle
- `POST /api/productos` crear
- `PUT /api/productos/{id}` actualizar
- `DELETE /api/productos/{id}` eliminar

- `POST /api/pedidos` crear pedido
- `POST /api/pedidos/{pedidoId}/agregar/{productoId}?cantidad=1` agregar producto
- `POST /api/pedidos/{pedidoId}/confirmar` confirmar
- `POST /api/pedidos/{pedidoId}/cancelar` cancelar

## Frontend
- `Front/script.js` usa `API_BASE = http://localhost:8080/api`.
- `Front/productos-manuales.js` define `globalThis.productosManual` con imágenes dentro de `Front/Productos/`.
# Techlab Gestión (Backend)

API RESTful en Java Spring Boot para gestionar productos, carrito y pedidos, integrable con tu frontend JS.

## Requisitos
- Java 17
- Maven
- MySQL en `localhost:3306` con DB `techlab_db` (configurable via env)

## Configuración
Variables de entorno (opcional):
- `DB_USER` (por defecto `root`)
- `DB_PASS` (por defecto `password`)

## Ejecutar
```powershell
mvn -v
mvn clean package
mvn spring-boot:run
```

## Endpoints principales (Tienda de electrodomésticos)
- Productos:
	- `GET /api/productos` listar
	- `GET /api/productos/{id}` obtener
	- `POST /api/productos` crear
	- `PUT /api/productos/{id}` actualizar
	- `DELETE /api/productos/{id}` eliminar
- Carrito:
	- `GET /api/carrito` ver contenido y total
	- `POST /api/carrito/agregar/{productoId}?cantidad=1` agregar producto
	- `POST /api/carrito/quitar/{productoId}?cantidad=1` quitar unidades
	- `DELETE /api/carrito/vaciar` vaciar carrito
- Pedidos:
	- `POST /api/pedidos` crear pedido
	- `POST /api/pedidos/{pedidoId}/agregar/{productoId}?cantidad=2` agregar línea al pedido
	- `POST /api/pedidos/{pedidoId}/confirmar` confirmar
	- `POST /api/pedidos/{pedidoId}/cancelar` cancelar
	- `GET /api/pedidos` listar
	- `GET /api/pedidos/{id}` obtener

## Tests
Perfil H2 en tests. Ejecuta:
```powershell
mvn test
```

## Nota
Categorías y usuarios se dejan como opcional/placeholder para extender.
