/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.leg.alrr.catalogo.model;

import br.leg.alrr.catalogo.util.BaseEntity;

/**
 *
 * @author Heliton
 */
public class Departamento implements BaseEntity{

    private Long id;
    
    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }
    
}
