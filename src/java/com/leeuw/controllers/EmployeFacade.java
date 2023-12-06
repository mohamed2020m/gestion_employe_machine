/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.leeuw.controllers;

import com.leeuw.entities.Employe;
import com.leeuw.entities.Machine;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author HP
 */
@Stateless
public class EmployeFacade extends AbstractFacade<Employe> {
    @PersistenceContext(unitName = "machine_employePU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public EmployeFacade() {
        super(Employe.class);
    }
    
//    public List<Machine> getMachinesByEmployee(int employeeId) {
//        String hql = "FROM Machine m WHERE m.employe.id = :employeeId";
//
//        Query query = em.createQuery(hql, Machine.class);
//        query.setParameter("employeeId", employeeId);
//
//        return query.getResultList();
//    }

}
