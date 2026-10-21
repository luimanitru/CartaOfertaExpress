package com.cartaofertaexpress.model;

import java.math.BigDecimal;

public class Candidato {

    private int id;

    private String nombre;
    private String cedula;
    private String cargo;
    private BigDecimal salario;
    private String rol;
    private String tipoContrato;
    private String fechaIngreso;
    private String ciudad;
    private String tasa;
    private String celular;
    private String correo;
    private String fechaCorreo;

    private boolean activo;
    private boolean resaltado;

    // =====================================================
    // DATOS DE LA CARTA
    // =====================================================

    private boolean cartaGenerada;

    private String archivoCarta;

    private String fechaCarta;

    private String plantillaCarta;

    // =====================================================
    // CONSTRUCTOR VACÍO
    // =====================================================

    public Candidato() {

        this.activo = true;
        this.resaltado = false;
        this.cartaGenerada = false;
    }

    // =====================================================
    // CONSTRUCTOR
    // =====================================================

    public Candidato(
            String nombre,
            String cedula,
            String cargo,
            BigDecimal salario,
            String rol,
            String tipoContrato,
            String fechaIngreso,
            String ciudad,
            String tasa,
            String celular,
            String correo
    ) {

        this.nombre = nombre;
        this.cedula = cedula;
        this.cargo = cargo;
        this.salario = salario;
        this.rol = rol;
        this.tipoContrato = tipoContrato;
        this.fechaIngreso = fechaIngreso;
        this.ciudad = ciudad;
        this.tasa = tasa;
        this.celular = celular;
        this.correo = correo;

        this.activo = true;

        this.cartaGenerada = false;
    }

    // =====================================================
    // ID
    // =====================================================
    public boolean isResaltado() {
        return resaltado;
    }

    public void setResaltado(boolean resaltado) {
        this.resaltado = resaltado;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    // =====================================================
    // NOMBRE
    // =====================================================

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    // =====================================================
    // CÉDULA
    // =====================================================

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    // =====================================================
    // CARGO
    // =====================================================

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    // =====================================================
    // SALARIO
    // =====================================================

    public BigDecimal getSalario() {
        return salario;
    }

    public void setSalario(BigDecimal salario) {
        this.salario = salario;
    }

    // =====================================================
    // ROL
    // =====================================================

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    // =====================================================
    // TIPO CONTRATO
    // =====================================================

    public String getTipoContrato() {
        return tipoContrato;
    }

    public void setTipoContrato(String tipoContrato) {
        this.tipoContrato = tipoContrato;
    }

    // =====================================================
    // FECHA INGRESO
    // =====================================================

    public String getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(String fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    // =====================================================
    // CIUDAD
    // =====================================================

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    // =====================================================
    // TASA
    // =====================================================

    public String getTasa() {
        return tasa;
    }

    public void setTasa(String tasa) {
        this.tasa = tasa;
    }

    // =====================================================
    // CELULAR
    // =====================================================

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    // =====================================================
    // CORREO
    // =====================================================

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    // =====================================================
// FECHA DE CORREO
// =====================================================

    public String getFechaCorreo() {
        return fechaCorreo;
    }

    public void setFechaCorreo(String fechaCorreo) {
        this.fechaCorreo = fechaCorreo;
    }

    // =====================================================
    // ACTIVO
    // =====================================================

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    // =====================================================
    // CARTA GENERADA
    // =====================================================

    public boolean isCartaGenerada() {
        return cartaGenerada;
    }

    public void setCartaGenerada(boolean cartaGenerada) {
        this.cartaGenerada = cartaGenerada;
    }

    // =====================================================
    // ARCHIVO CARTA
    // =====================================================

    public String getArchivoCarta() {
        return archivoCarta;
    }

    public void setArchivoCarta(String archivoCarta) {
        this.archivoCarta = archivoCarta;
    }

    // =====================================================
    // FECHA CARTA
    // =====================================================

    public String getFechaCarta() {
        return fechaCarta;
    }

    public void setFechaCarta(String fechaCarta) {
        this.fechaCarta = fechaCarta;
    }

    // =====================================================
    // PLANTILLA CARTA
    // =====================================================

    public String getPlantillaCarta() {
        return plantillaCarta;
    }

    public void setPlantillaCarta(String plantillaCarta) {
        this.plantillaCarta = plantillaCarta;
    }

    // =====================================================
    // TOSTRING
    // =====================================================

    @Override
    public String toString() {

        return "Candidato{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", cedula='" + cedula + '\'' +
                ", cargo='" + cargo + '\'' +
                ", salario=" + salario +
                ", rol='" + rol + '\'' +
                ", tipoContrato='" + tipoContrato + '\'' +
                ", fechaIngreso='" + fechaIngreso + '\'' +
                ", ciudad='" + ciudad + '\'' +
                ", tasa='" + tasa + '\'' +
                ", celular='" + celular + '\'' +
                ", correo='" + correo + '\'' +
                ", activo=" + activo +
                ", cartaGenerada=" + cartaGenerada +
                ", archivoCarta='" + archivoCarta + '\'' +
                ", fechaCarta='" + fechaCarta + '\'' +
                ", plantillaCarta='" + plantillaCarta + '\'' +
                '}';
    }
}