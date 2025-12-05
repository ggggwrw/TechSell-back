package com.techlab.dto;

import jakarta.validation.constraints.*;

public class ProductoDto {
    private Long id;
    @NotBlank
    private String nombre;
    @Positive
    private double precio;
    @Min(0)
    private int stock;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public double getPrecio() { return precio; }
    public void setPrecio(double precio) { this.precio = precio; }
    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }
}
