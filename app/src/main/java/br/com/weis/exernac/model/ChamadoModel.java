package br.com.weis.exernac.model;

import java.util.Date;

/**
 * Created by logonrm on 29/08/2017.
 */

public class ChamadoModel {
    private int codigo;
    private int codigoFuncionario;
    private Date data;
    private boolean finalizado;
    private String descricao;

    public ChamadoModel(int codigoFuncionario, boolean finalizado, String descricao) {
        this.codigoFuncionario = codigoFuncionario;
        this.finalizado = finalizado;
        this.descricao = descricao;
    }

    public ChamadoModel(int codigo, int codigoFuncionario, Date data, boolean finalizado, String descricao) {
        this.codigo = codigo;
        this.codigoFuncionario = codigoFuncionario;
        this.data = data;
        this.finalizado = finalizado;
        this.descricao = descricao;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public int getCodigoFuncionario() {
        return codigoFuncionario;
    }

    public void setCodigoFuncionario(int codigoFuncionario) {
        this.codigoFuncionario = codigoFuncionario;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public boolean isFinalizado() {
        return finalizado;
    }

    public void setFinalizado(boolean finalizado) {
        this.finalizado = finalizado;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    @Override
    public String toString() {
        return codigo + " - " + descricao + " - " + data + " Finalizado: " + finalizado;
    }
}
