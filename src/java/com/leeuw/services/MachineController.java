package com.leeuw.services;

import com.leeuw.entities.Machine;
import com.leeuw.services.util.JsfUtil;
import com.leeuw.services.util.JsfUtil.PersistAction;
import com.leeuw.controllers.MachineFacade;
import com.leeuw.entities.Employe;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.CartesianChartModel;
import org.primefaces.model.chart.ChartSeries;

@ManagedBean(name = "machineController")
@SessionScoped
public class MachineController implements Serializable {
    
    @EJB
    private com.leeuw.controllers.MachineFacade ejbFacade;
    private List<Machine> items = null;
    private Machine selected;
    
    
    @ManagedProperty(value = "#{employeController}")
    private EmployeController employeBean;

    private List<Machine> machines;
    
    
    private BarChartModel machineByEmployeeModel;

    public BarChartModel getMachineByEmployeeModel() {
        return machineByEmployeeModel;
    }

    private void createMachineByEmployeeModel() {
        machineByEmployeeModel = new BarChartModel();
        ChartSeries machinesByEmployeeSeries = new ChartSeries();
        machinesByEmployeeSeries.setLabel("Machines par Employé");

        List<Object[]> dataByEmployee = ejbFacade.getMachinesByEmployeeChart();

        for (Object[] entry : dataByEmployee) {
            Integer employeeId = (Integer) entry[0];
            Long machineCount = (Long) entry[1];

            // Retrieve the employee name based on the employeeId
            String employeeName = ejbFacade.getEmployeeNameById(employeeId);

            // Null check
            if (employeeName != null) {
                // Log the data for debugging
                System.out.println("Employee Name: " + employeeName + ", Machine Count: " + machineCount);

                machinesByEmployeeSeries.set(employeeName, machineCount);
            } else {
                // Handle the case where no employee name is found
                System.out.println("Employee Name not found for ID: " + employeeId);
            }
        }


        machineByEmployeeModel.addSeries(machinesByEmployeeSeries);

        Axis xAxis = machineByEmployeeModel.getAxis(AxisType.X);
        xAxis.setLabel("Employé");

        Axis yAxis = machineByEmployeeModel.getAxis(AxisType.Y);
        yAxis.setLabel("Nombre de Machines");
    }

    
    
    private BarChartModel machineByYearModel;

    @PostConstruct
    public void init() {
        createMachineByYearModel();
    }

    public BarChartModel getMachineByYearModel() {
        return machineByYearModel;
    }

    private void createMachineByYearModel() {
    machineByYearModel = new BarChartModel();
    ChartSeries machinesSeries = new ChartSeries();
    machinesSeries.setLabel("Machines Acquises");

    List<Object[]> dataByYear = ejbFacade.getMachinesByYear();

    for (Object[] entry : dataByYear) {
        Integer year = (Integer) entry[0];
        Long machineCount = (Long) entry[1];

        // Log the data for debugging
        System.out.println("Year: " + year + ", Machine Count: " + machineCount);

        machinesSeries.set(String.valueOf(year), machineCount);
    }

    machineByYearModel.addSeries(machinesSeries);

    Axis xAxis = machineByYearModel.getAxis(AxisType.X);
    xAxis.setLabel("Année");

    Axis yAxis = machineByYearModel.getAxis(AxisType.Y);
    yAxis.setLabel("Nombre de Machines");
}

    

    
    public void loadMachinesByEmployee() {
        Employe selectedEmployee = employeBean.getSelected();
        if (selectedEmployee != null) {
            machines = ejbFacade.getMachinesByEmployee(selectedEmployee);
        } else {
            // Handle the case where no employee is selected
            machines = null;
        }
    }

    public EmployeController getEmployeBean() {
        return employeBean;
    }

    public void setEmployeBean(EmployeController employeBean) {
        this.employeBean = employeBean;
    }
    
    

    public List<Machine> getMachines() {
        return machines;
    }

    public void setMachines(List<Machine> machines) {
        this.machines = machines;
    }
    
    
    public MachineController() {
    }

    
    public Machine getSelected() {
        return selected;
    }

    public void setSelected(Machine selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private MachineFacade getFacade() {
        return ejbFacade;
    }

    public Machine prepareCreate() {
        selected = new Machine();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("MachineCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("MachineUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("MachineDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<Machine> getItems() {
        if (items == null) {
            items = getFacade().findAll();
        }
        return items;
    }

    private void persist(PersistAction persistAction, String successMessage) {
        if (selected != null) {
            setEmbeddableKeys();
            try {
                if (persistAction != PersistAction.DELETE) {
                    getFacade().edit(selected);
                } else {
                    getFacade().remove(selected);
                }
                JsfUtil.addSuccessMessage(successMessage);
            } catch (EJBException ex) {
                String msg = "";
                Throwable cause = ex.getCause();
                if (cause != null) {
                    msg = cause.getLocalizedMessage();
                }
                if (msg.length() > 0) {
                    JsfUtil.addErrorMessage(msg);
                } else {
                    JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
                }
            } catch (Exception ex) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
                JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            }
        }
    }

    public List<Machine> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<Machine> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = Machine.class)
    public static class MachineControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            MachineController controller = (MachineController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "machineController");
            return controller.getFacade().find(getKey(value));
        }

        java.lang.Integer getKey(String value) {
            java.lang.Integer key;
            key = Integer.valueOf(value);
            return key;
        }

        String getStringKey(java.lang.Integer value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value);
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Machine) {
                Machine o = (Machine) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Machine.class.getName()});
                return null;
            }
        }

    }
 
}
