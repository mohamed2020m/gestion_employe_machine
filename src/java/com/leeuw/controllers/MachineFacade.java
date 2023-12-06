/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.leeuw.controllers;

import com.leeuw.entities.Employe;
import com.leeuw.entities.Machine;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author HP
 */
@Stateless
public class MachineFacade extends AbstractFacade<Machine> {
    @PersistenceContext(unitName = "machine_employePU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public MachineFacade() {
        super(Machine.class);
    }
    
    public List<Machine> getMachinesByEmployee(Employe employee) {
        Query query = em.createQuery("SELECT m FROM Machine m WHERE m.employe = :employee");
        query.setParameter("employee", employee);
        return query.getResultList();
    }
    
    public List<Object[]> getMachinesByEmployeeChart() {
        Query query = em.createQuery("SELECT m.employe.id, COUNT(m) FROM Machine m GROUP BY m.employe.id");
        return query.getResultList();
    }

    public String getEmployeeNameById(Integer employeeId) {
        Query query = em.createQuery("SELECT e.name FROM Employe e WHERE e.id = :employeeId");
        query.setParameter("employeeId", employeeId);

        try {
            return (String) query.getSingleResult();
        } catch (NoResultException e) {
            // Handle the case where no employee is found with the given ID
            return null;
        }
    }


    
    public List<Object[]> getMachinesByYear() {
        Query query = em.createQuery("SELECT EXTRACT(YEAR FROM m.dateAchat), COUNT(m) FROM Machine m GROUP BY EXTRACT(YEAR FROM m.dateAchat)");
        return query.getResultList();
    }
}
