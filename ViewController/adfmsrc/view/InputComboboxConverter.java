package view;

import java.util.HashMap;
import java.util.List;

import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import oracle.adf.model.binding.DCIteratorBinding;
import oracle.adf.view.rich.component.rich.input.RichInputComboboxListOfValues;
import oracle.adf.view.rich.model.ColumnDescriptor;
import oracle.adf.view.rich.model.ListOfValuesModel;

import oracle.jbo.AttributeDef;
import oracle.jbo.uicli.binding.JUCtrlHierBinding;

import org.apache.myfaces.trinidad.model.CollectionModel;

public class InputComboboxConverter implements Converter {
    private Map<String, Object> _items;
    
    public InputComboboxConverter() {
        super();
    }

    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent uIComponent, String string) {
        System.out.println("InputComboboxConverter.getAsObject: " + string);
        Object object = _items.get(string);
        if (object != null) {
            System.out.println("InputComboboxConverter.getAsObject.object: " + object);
            return object;
        } else {
            System.out.println("InputComboboxConverter.getAsObject.string: " + string);
            String value = getValue(uIComponent, string);
            if (value != null) {
                return string;
            } else {
                return null;
            }
        }
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent uIComponent, Object object) {
        System.out.println("InputComboboxConverter.getAsString: " + object);
        if (object == null) {
            return null;
        }

        _items = new HashMap<String, Object>();
        String string = getValue(uIComponent, object);
        System.out.println("InputComboboxConverter.getAsString.string: " + string);
        _items.put(string, object);
        return string;
    }
    
    private String getValue(UIComponent uIComponent, Object object) {
        if (uIComponent instanceof RichInputComboboxListOfValues) {
            RichInputComboboxListOfValues rcl = (RichInputComboboxListOfValues) uIComponent;
            ListOfValuesModel lov = rcl.getModel();
            CollectionModel collectionModel = lov.getTableModel().getCollectionModel(); 
            List<ColumnDescriptor> descriptors = lov.getItemDescriptors();
            List<Map<Object, String>> items = (List<Map<Object, String>>) lov.getItems();
            JUCtrlHierBinding treeBinding = (JUCtrlHierBinding) collectionModel.getWrappedData();
            DCIteratorBinding iter = treeBinding.getDCIteratorBinding();

            AttributeDef[] keyAttrs = iter.getViewObject().getKeyAttributeDefs();
            String keyAtrr = keyAttrs[0].getName();
            
            for (Map<Object, String> item : items) {
                if (item.containsKey(keyAtrr) && item.containsValue(object)) {
                    String value = "";
                    for (ColumnDescriptor cd : descriptors) {
                        value += (value.isEmpty() ? "" : " ") + String.valueOf(item.get(cd.getName()));
                    }
                    return value;
                }
            }
        }
        return null;
    }
}
