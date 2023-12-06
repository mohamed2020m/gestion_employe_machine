
import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author HP
 */


@ManagedBean(name="language", eager = true)
@SessionScoped
public class LanguageBean implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private FacesContext context = FacesContext.getCurrentInstance();

    private String localeCode;
    
    private static Map<String, Object> countries;
    
    static {
        countries = new LinkedHashMap<String, Object>();
        countries.put("English", Locale.ENGLISH); // label, value
        countries.put("Francaise", Locale.FRENCH);
    }
    
     
    public Map<String, Object> getCountriesInMap() {
        return countries;
    }

    public static Map<String, Object> getCountries() {
        return countries;
    }

    public static void setCountries(Map<String, Object> countries) {
        LanguageBean.countries = countries;
    }
    
    
    public String getLocaleCode() {
        return localeCode;
    }

    public void setLocaleCode(String localeCode) {
        this.localeCode = localeCode;
    }

    public void countryLocaleCodeChanged(ValueChangeEvent e) {
        String newLocaleValue = e.getNewValue().toString();
        System.out.println("New Locale Value: " + newLocaleValue);

        for (Map.Entry<String, Object> entry : countries.entrySet()) {
            if (entry.getValue().toString().equals(newLocaleValue)) {
                FacesContext context = FacesContext.getCurrentInstance();
                context.getViewRoot().setLocale((Locale) entry.getValue());
                System.out.println("Locale set to: " + entry.getValue());
            }
        }
    }

}