package view;

import java.util.ArrayList;
import java.util.List;

import java.util.Map;

import javax.faces.model.SelectItem;

import oracle.adf.model.binding.DCIteratorBinding;
import oracle.adf.view.rich.component.rich.input.RichInputComboboxListOfValues;
import oracle.adf.view.rich.model.ColumnDescriptor;
import oracle.adf.view.rich.model.ListOfValuesModel;

import oracle.jbo.AttributeDef;
import oracle.jbo.uicli.binding.JUCtrlHierBinding;

import org.apache.myfaces.trinidad.model.CollectionModel;

public class IndexBean {
    private RichInputComboboxListOfValues iclov;
    private String returnValue;

    public IndexBean() {
    }

    public List<SelectItem> suggestedItems(String string) {
        ListOfValuesModel lov = iclov.getModel();
        List<SelectItem> suggestedItems = new ArrayList<SelectItem>();
        CollectionModel collectionModel = lov.getTableModel().getCollectionModel(); 
        List<ColumnDescriptor> descriptors = lov.getItemDescriptors();
        List<Map<String, Object>> items = (List<Map<String, Object>>) lov.getItems();
        JUCtrlHierBinding treeBinding = (JUCtrlHierBinding) collectionModel.getWrappedData();
        DCIteratorBinding iter = treeBinding.getDCIteratorBinding();

        AttributeDef[] keyAttrs = iter.getViewObject().getKeyAttributeDefs();
        String keyAtrr = keyAttrs[0].getName();
        
        for (Map<String, Object> item : items) {
            boolean found = false;
            for (Map.Entry entry : item.entrySet()) {
                if (!keyAtrr.equals(entry.getKey()) && entry.getValue().toString().toUpperCase().contains(string.toUpperCase())) {
                    found = true;
                }
            }
            
            if (found) {
                String value = "";
                for (ColumnDescriptor cd : descriptors) {
                    value += (value.isEmpty() ? "" : " ") + item.get(cd.getName());
                }
                suggestedItems.add(new SelectItem(item.get(keyAtrr), value));
            }
        }
        return suggestedItems;
    }

    public void setIclov(RichInputComboboxListOfValues iclov) {
        this.iclov = iclov;
    }

    public RichInputComboboxListOfValues getIclov() {
        return iclov;
    }

    public void setReturnValue(String returnValue) {
        this.returnValue = returnValue;
    }

    public String getReturnValue() {
        return returnValue;
    }
}
