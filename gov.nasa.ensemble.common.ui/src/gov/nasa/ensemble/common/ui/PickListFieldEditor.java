/*******************************************************************************
 * Copyright 2014 United States Government as represented by the
 * Administrator of the National Aeronautics and Space Administration.
 * All Rights Reserved.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package gov.nasa.ensemble.common.ui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.preference.FieldEditor;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Widget;

/**
 * An abstract field editor that manages a list of input values. 
 * The editor displays a list containing the values, buttons for
 * adding and removing values, and Up and Down buttons to adjust
 * the order of elements in the list.
 * <p>
 * Subclasses must implement the <code>parseString</code>,
 * <code>createList</code>, and <code>getNewInputObject</code>
 * framework methods.
 * </p>
 */
public class PickListFieldEditor extends FieldEditor {

	/**
     * The parent, needed when used by a wizard page.
     */
    private Control top;
	
	/**
     * The list widget that contains all of the possible
     * choices for the selectedItemsList
     */
    private List pickableItemsList;

    /**
     * The list widget that contains all of the selected
     * choices. These are the items that get saved as 
     * preferences
     */
    private List selectedItemsList;
    
    /**
     * The column (box) on the left containing the pickable items and their search box.
     * <code>null</code> if none (before creation or after disposal).
     */
    private Composite leftColumn;
    
    /**
     * The button box containing the Add, Remove, Up, and Down buttons;
     * <code>null</code> if none (before creation or after disposal).
     */
    private Composite buttonBox;

    /**
     * The Add button.
     */
    private Button addButton;

    /**
     * The Add all button.
     */
    private Button addAllButton;

    /**
     * The Remove button.
     */
    private Button removeButton;

    /**
     * The Remove all button.
     */
    private Button removeAllButton;
    
    /**
     * Original list as passed into the constructor for sorting purposes
     */
    private ArrayList<String> orginalPickableList;

    /**
     * The available items to select
     */
    private ArrayList<String> pickableItems;

    /**
     * Whether or not to use the up/down buttons
     */
	private final boolean ordered;

    /**
     * The Up button.
     */
    private Button upButton;

    /**
     * The Down button.
     */
    private Button downButton;

    /**
     * The selection listener.
     */
    private SelectionListener selectionListener;

    /**
     * Preference change listener to update the pick list
     */
    private IPropertyChangeListener listener = new PropertyChangeListenerImpl();
    
	private Comparator<String> comparator = new PickableListComparator();
	
	/**
	 * The user can filter the elements in the view by entering search criteria in the filter text widget  
	 */
	private Text searchBox  = null;

    /**
     * Creates a list field editor.
     * 
     * @param name the name of the preference this field editor works on
     * @param labelText the label text of the field editor
     * @param parent the parent of the field editor's control
     * @param ordered indicates if there is UI support for ordering (i.e. 'Up' & 'Down')
     */
    public PickListFieldEditor(String name, String labelText, Composite parent, Collection<String> pickableItems, boolean ordered) {
        this.ordered = ordered;
		this.pickableItems = new ArrayList<String>(pickableItems);
		this.orginalPickableList = new ArrayList<String>(pickableItems);
        init(name, labelText);
        createControl(parent);
    }
    
    @Override
	public void setPreferenceStore(IPreferenceStore store) {
    	if (getPreferenceStore() != null) {
    		getPreferenceStore().removePropertyChangeListener(listener);
    	}
		super.setPreferenceStore(store);
		if (getPreferenceStore() != null) {
    		getPreferenceStore().addPropertyChangeListener(listener);
    	}
	}



	/* (non-Javadoc)
     * Method declared on FieldEditor.
     */
    @Override
	protected void adjustForNumColumns(int numColumns) {
    	// no implementation
    }

    /**
     * Creates the Add, Remove, Up, and Down button in the given button box.
     *
     * @param box the box for the buttons
     */
    private void createButtons(Composite box) {
        addButton = createPushButton(box, "Add");
        removeButton = createPushButton(box, "Remove");
        addAllButton = createPushButton(box, "Add All");
        removeAllButton = createPushButton(box, "Remove All");
        if (ordered) {
	        upButton = createPushButton(box, "Up");
	        downButton = createPushButton(box, "Down");
        }
	  }

    /**
     * Combines the given list of items into a single string.
     * This method is the converse of <code>parseString</code>. 
     *
     * @param items the list of items
     * @return the combined string
     * @see #parseString
     */
    protected String createList(String[] items) {
		StringBuffer buffer = new StringBuffer();
		for(int i=0; i<items.length; i++) {
			if (i > 0) {
				buffer.append(",");
			}
			buffer.append(items[i]);
		}
		return buffer.toString();
    }

    /**
     * Helper method to create a push button.
     * 
     * @param parent the parent control
     * @param key the resource name used to supply the button's label text
     * @return Button
     */
    private Button createPushButton(Composite parent, String text) {
        Button button = new Button(parent, SWT.PUSH);
        button.setText(text);
        button.setFont(parent.getFont());
        GridData data = new GridData(GridData.FILL_HORIZONTAL);
        int widthHint = convertHorizontalDLUsToPixels(button, IDialogConstants.BUTTON_WIDTH);
        data.widthHint = Math.max(widthHint, button.computeSize(SWT.DEFAULT, SWT.DEFAULT, true).x);
        button.setLayoutData(data);
        button.addSelectionListener(getSelectionListener());
        return button;
    }

    /**
     * Creates a selection listener.
     */
    public void createSelectionListener() {
        selectionListener = new SelectionAdapter() {
            @Override
			public void widgetSelected(SelectionEvent event) {
                Widget widget = event.widget;
                if (widget == addButton) {
                    addPressed();
                } else if (widget == removeButton) {
                    removePressed();
                } else if (widget == addAllButton) {
                    addAllPressed();
                } else if (widget == removeAllButton) {
                    removeAllPressed();
                } else if (ordered && (widget == upButton)) {
                    upPressed();
                } else if (ordered && (widget == downButton)) {
                    downPressed();
                } else if (widget instanceof List) {
                    selectionChanged();
                }
            }
        };
    }
    
    public Control getTopLevelControl () {
    	return top;
    }

    /* (non-Javadoc)
     * Method declared on FieldEditor.
     */
    @Override
	protected void doFillIntoGrid(Composite parent, int numColumns) {
    	top = parent;
    	
    	Point textExtent = computeTextExtent(parent);
    	int heightHint = Math.max(50, Math.min(textExtent.y, 300));
    	int widthHint = Math.max(50, Math.min(textExtent.x, 200));
    	GridData gd; // reinitialized several times below -- each widget needs its own

    	Composite outerComposite = new Composite(parent, SWT.NONE);
    	outerComposite.setLayout(new RowLayout(SWT.VERTICAL));
    	gd = new GridData(GridData.FILL_HORIZONTAL);
    	outerComposite.setLayoutData(gd);
    	gd.horizontalSpan = 99;
    	
    	Label label = new Label(outerComposite, SWT.NONE);
        label.setText(getLabelText());
		Group group = new Group(outerComposite, SWT.NONE);
    	group.setLayout(new GridLayout ());

    	{
    		Composite searchLabelAndBox = new Composite(group, SWT.NONE);
    		GridLayout layout = new GridLayout();
    		new Label(searchLabelAndBox, SWT.NONE).setText("Type here to search:");
    		layout.numColumns = 2;
    		layout.marginHeight = 0;
    		layout.marginWidth = 0;
    		searchLabelAndBox.setLayout(layout);
    		searchLabelAndBox.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
    		searchBox = new Text(searchLabelAndBox, SWT.SEARCH | SWT.CANCEL);
    		searchBox.setData("name", "ActivityDictionaryView.findFilterText");
    		searchBox.setToolTipText("Type here to filter choices on the left.");
    		searchBox.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
    		searchBox.addModifyListener(new SearchBoxModifyListener());
    		searchBox.addSelectionListener(new SearchBoxSelectionListener());
    	}
    	Composite leftboxButtonsRightbox = new Composite(group, SWT.NONE);
    	{
    		GridLayout layout = new GridLayout();
    		layout.numColumns = 3;
    		layout.marginHeight = 0;
    		layout.marginWidth = 0;
    		leftboxButtonsRightbox.setLayout(layout);
    	}

		leftColumn = getLeftColumnControl(leftboxButtonsRightbox);
        gd = new GridData(GridData.FILL_HORIZONTAL);
        gd.verticalAlignment = GridData.FILL;
		gd.heightHint = heightHint;
		gd.widthHint = widthHint + pickableItemsList.getBorderWidth();
        gd.grabExcessHorizontalSpace = true;
        pickableItemsList.setLayoutData(gd);
        
        buttonBox = getButtonBoxControl(leftboxButtonsRightbox);
        gd = new GridData();
        gd.verticalAlignment = GridData.BEGINNING;
        buttonBox.setLayoutData(gd);
        
        selectedItemsList = getSelectedListControl(leftboxButtonsRightbox);
        gd = new GridData(GridData.FILL_HORIZONTAL);
        gd.verticalAlignment = GridData.FILL;
        gd.heightHint = heightHint;
        gd.widthHint = widthHint + selectedItemsList.getBorderWidth();
        gd.grabExcessHorizontalSpace = true;
        selectedItemsList.setLayoutData(gd);
        doLoad(selectedItemsList.getItems());
    }
    
	private void searchCancelled() {
		searchBox.setText("");
	}

	private Point computeTextExtent(Composite parent) {
		StringBuilder builder = new StringBuilder();
		for (String string : pickableItems) {
			builder.append(string);
			builder.append('\n');
		}
		GC gc = new GC(parent);
		Point textExtent = gc.textExtent(builder.toString(), SWT.DRAW_DELIMITER);
		gc.dispose();
    	return textExtent;
	}

    /* (non-Javadoc)
     * Method declared on FieldEditor.
     */
    @Override
	protected void doLoad() {
        doLoad(getPreferenceStore().getString(getPreferenceName()));
    }

    /* (non-Javadoc)
     * Method declared on FieldEditor.
     */
    @Override
	protected void doLoadDefault() {
        doLoad(getPreferenceStore().getDefaultString(getPreferenceName()));
    }
    
    protected void doLoad(String commaSeparated) {
        doLoad(parseString(commaSeparated));
    }
    
    protected void doLoad(String[] showOnRight) {
         doLoad(Arrays.asList(showOnRight));
    }
    
    protected void doLoad(java.util.List<String> showOnRight) {
        if (pickableItemsList != null && selectedItemsList != null) {
	        pickableItemsList.removeAll();
	        selectedItemsList.removeAll();
	        
	        Set<String> set = new HashSet<String>(showOnRight);
	        String searchFor = searchBox.getText().toUpperCase();
	        String [] defaults = pickableItems.toArray(new String[0]);
	        for(int i = 0; i <defaults.length; i++) {
	        	String item = defaults[i];
	        	if (set.contains(item)) {
	        		continue;
	        	}
	        	
	        	if (searchFor==null 
	        			|| searchFor.length()==0
	        			|| item.toUpperCase().contains(searchFor)) {
	        		pickableItemsList.add(item);
	        	}
	        }
	        
	    	for (String item : showOnRight) {
	            selectedItemsList.add(item);
    		}
        }
    }

    /* (non-Javadoc)
     * Method declared on FieldEditor.
     */
    @Override
	protected void doStore() {
        String s = createList(selectedItemsList.getItems());
        if (s != null) {
			getPreferenceStore().setValue(getPreferenceName(), s);
		}
    }

    /**
     * Notifies that the Down button has been pressed.
     */
    private void downPressed() {
        move(false);
    }

    /**
     * Returns this field editor's button box containing the Add, Remove,
     * Up, and Down button.
     *
     * @param parent the parent control
     * @return the button box
     */
    public Composite getButtonBoxControl(Composite parent) {
        if (buttonBox == null) {
            buttonBox = new Composite(parent, SWT.NULL);
            GridLayout layout = new GridLayout();
            layout.marginWidth = 0;
            buttonBox.setLayout(layout);
            createButtons(buttonBox);
            buttonBox.addDisposeListener(new DisposeListener() {
                @Override
				public void widgetDisposed(DisposeEvent event) {
                    addButton = null;
                    removeButton = null;
                    addAllButton = null;
                    removeAllButton = null;
                    upButton = null;
                    downButton = null;
                    buttonBox = null;
                 }});
        } else {
            checkParent(buttonBox, parent);
        }
        selectionChanged();
        return buttonBox;
    }

    /**
      * Returns this field editor's left column containing the unpicked-but=pickable items
      * and the search box to filter them.
      * @param parent the parent control
      * @return the column
      */
    private Composite getLeftColumnControl(Composite parent) {
    	if (leftColumn == null) {
    		leftColumn = new Composite(parent, SWT.NULL);
    		GridLayout layout = new GridLayout();
    		layout.marginWidth = 0;
    		layout.marginHeight = 0;
    		leftColumn.setLayout(layout);
    		pickableItemsList = getPickableListControl(leftColumn);
    		pickableItemsList.setLayoutData(new GridData(GridData.FILL_VERTICAL | GridData.GRAB_HORIZONTAL));
    		leftColumn.addDisposeListener(new DisposeListener() {
    			@Override
				public void widgetDisposed(DisposeEvent event) {
    				leftColumn = null;
    				pickableItemsList = null;
     			}});
    	} else {
    		checkParent(leftColumn, parent);
    	}
    	return leftColumn;
    }

    public List getPickableListControl() {
    	return pickableItemsList;
    }
    
    /**
     * Returns this field editor's list control.
     *
     * @param parent the parent control
     * @return the list control
     */
    public List getPickableListControl(Composite parent) {
        if (pickableItemsList == null) {
        	pickableItemsList = createList(parent);
        	pickableItemsList.addDisposeListener(new DisposeListener() {
                @Override
				public void widgetDisposed(DisposeEvent event) {
                	pickableItemsList = null;
                }
            });
        }
        return pickableItemsList;
    }

    public List getSelectedListControl() {
    	return selectedItemsList;
    }

    public List getSelectedListControl(Composite parent) {
        if (selectedItemsList == null) {
        	selectedItemsList = createList(parent);
        	selectedItemsList.addDisposeListener(new DisposeListener() {
                @Override
				public void widgetDisposed(DisposeEvent event) {
                	selectedItemsList = null;
                }
            });
        }
        return selectedItemsList;
    }
    
    private List createList(Composite parent) {
    	List list = new List(parent, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL | SWT.MULTI);
    	list.setFont(parent.getFont());
    	list.addSelectionListener(getSelectionListener());
    	return list;
    }
    
    /* (non-Javadoc)
     * Method declared on FieldEditor.
     */
    @Override
	public int getNumberOfControls() {
        return 1;
    }

    /**
     * Returns this field editor's selection listener.
     * The listener is created if nessessary.
     *
     * @return the selection listener
     */
    private SelectionListener getSelectionListener() {
        if (selectionListener == null) {
			createSelectionListener();
		}
        return selectionListener;
    }

    /**
     * Returns this field editor's shell.
     * <p>
     * This method is internal to the framework; subclassers should not call
     * this method.
     * </p>
     *
     * @return the shell
     */
    protected Shell getShell() {
        if (addButton == null) {
			return null;
		}
        return addButton.getShell();
    }

    /**
     * Splits the given string into a list of strings.
     * This method is the converse of <code>createList</code>. 
     * <p>
     * Subclasses must implement this method.
     * </p>
     *
     * @param stringList the string
     * @return an array of <code>String</code>
     * @see #createList
     */
    protected String[] parseString(String stringList) {
		StringTokenizer tokenizer = new StringTokenizer(stringList, ",");
		ArrayList<String> items = new ArrayList<String>();
		while(tokenizer.hasMoreTokens()) {
			items.add(tokenizer.nextToken().trim());
		}
		return items.toArray(new String[items.size()]);
    }

    /**
     * Notifies that the Remove button has been pressed.
     */
    private void removePressed() {
        setPresentsDefaultValue(false);
        int indices[] = selectedItemsList.getSelectionIndices();
        String items[] = new String[indices.length];
        for(int i = 0; i < indices.length; i++) {
        	items[i] = selectedItemsList.getItem(indices[i]);
        }
        
        for(int i = 0; i < items.length; i++) {
        	selectedItemsList.remove(items[i]);
        	insertItem(pickableItemsList, items[i]);
        }
        
        int newIndices[] = new int[items.length];
        for(int i = 0; i < newIndices.length; i++) {
        	newIndices[i] = pickableItemsList.indexOf(items[i]);
        }
        
        selectedItemsList.deselectAll();
        pickableItemsList.select(newIndices);
        selectionChanged();
    }
    
    private void removeAllPressed() {
        setPresentsDefaultValue(false);
        pickableItemsList.removeAll();
        selectedItemsList.removeAll();
    	for (String string : pickableItems) {
            pickableItemsList.add(string);
        }
        pickableItemsList.deselectAll();
        pickableItemsList.selectAll();
        selectionChanged();
    }

    /**
     * Notifies that the Add button has been pressed.
     */
    private void addPressed() {
    	int indices[] = pickableItemsList.getSelectionIndices();
    	String items[] = new String[indices.length];
    	for(int i=0; i<indices.length; i++) {
    		items[i] = pickableItemsList.getItem(indices[i]);
    	}
    	
    	for(int i=0; i<items.length; i++) {
    		pickableItemsList.remove(items[i]);
    		if (ordered) {
    			// insert at the end
    			selectedItemsList.add(items[i]);
    		} else {
    			// insert in the middle of the sorted list
    			insertItem(selectedItemsList, items[i]);
    		}
    	}
        
        int newIndices[] = new int[items.length];
        for(int i = 0; i < newIndices.length; i++) {
        	newIndices[i] = selectedItemsList.indexOf(items[i]);
        }
    	
        setPresentsDefaultValue(false);
        pickableItemsList.deselectAll();
        selectedItemsList.select(newIndices);
        selectionChanged();
    }

    private void addAllPressed() {
        setPresentsDefaultValue(false);
        selectedItemsList.deselectAll();
           	for (String string : pickableItemsList.getItems()) { // all items after filtering
    		selectedItemsList.add(string);
    		selectedItemsList.select(selectedItemsList.indexOf(string));
        }
    	pickableItemsList.removeAll();
        pickableItemsList.deselectAll();
        selectionChanged();
    }

    private void insertItem(List list, String item) {
    	java.util.List<String> items = Arrays.asList(list.getItems());
    	java.util.List<String> sortedItems = new ArrayList<String>(items);
    	sortedItems.add(item);
    	Collections.sort(sortedItems, comparator);
    	list.add(item, sortedItems.indexOf(item));
    }
    
    /**
     * Notifies that the list selection has changed.
     */
    private void selectionChanged() {
    	int index = (selectedItemsList != null ? selectedItemsList.getSelectionIndex() : -1);
    	int size = (selectedItemsList != null ? selectedItemsList.getItemCount() : 0);

    	java.util.List<String> oldSelectedItemsList = 
    		selectedItemsList == null ? null : Arrays.asList(selectedItemsList.getItems());
    	
        addButton.setEnabled(pickableItemsList != null && pickableItemsList.getItemCount() > 0);
		removeButton.setEnabled(index >= 0);

        addAllButton.setEnabled(pickableItemsList != null && pickableItemsList.getItemCount() > 0);
		removeAllButton.setEnabled(size > 0);

        if (ordered) {
        	index = getSelectedItemsSelectionIndex(true);
	        upButton.setEnabled(index > 0);
	        index = getSelectedItemsSelectionIndex(false);
	        downButton.setEnabled(index >= 0 && index < size - 1);
        }
        
        fireValueChanged(VALUE, oldSelectedItemsList, selectedItemsList == null ? null : Arrays.asList(selectedItemsList));
    }

    /* (non-Javadoc)
     * Method declared on FieldEditor.
     */
    @Override
	public void setFocus() {
        if (pickableItemsList != null) {
            pickableItemsList.setFocus();
        }
    }

    /**
     * Moves the currently selected item up or down.
     *
     * @param up <code>true</code> if the item should move up,
     *  and <code>false</code> if it should move down
     */
    private void move(boolean up) {
        setPresentsDefaultValue(false);
        String[] selection = selectedItemsList.getSelection();
        if (up) {
	        for (String item : selection) {
	        	int i = selectedItemsList.indexOf(item);
	        	selectedItemsList.remove(item);
	        	selectedItemsList.add(item, i-1);
	        }
        } else {
        	for (int i=selection.length-1; i>=0; i--) {
        		String item = selection[i];
	        	int j = selectedItemsList.indexOf(item);
	        	selectedItemsList.remove(item);
	        	selectedItemsList.add(item, j+1);
	        }
        }
        selectedItemsList.setSelection(selection);
        selectionChanged();
    }

	private int getSelectedItemsSelectionIndex(boolean up) {
		if (selectedItemsList == null) {
			return -1;
		}
		
		int[] indices = selectedItemsList.getSelectionIndices();
        int index = up ? Integer.MAX_VALUE : Integer.MIN_VALUE;
        for (int i : indices) {
        	if (up && i < index) {
    			index = i;
    		} else if (!up && i > index) {
    			index = i;
        	}
        }
		return index;
	}

    /**
     * Notifies that the Up button has been pressed.
     */
    private void upPressed() {
        move(true);
    }

    /*
     * @see FieldEditor.setEnabled(boolean,Composite).
     */
    @Override
	public void setEnabled(boolean enabled, Composite parent) {
        super.setEnabled(enabled, parent);
        getPickableListControl(parent).setEnabled(enabled);
        addButton.setEnabled(enabled);
        removeButton.setEnabled(enabled);
        addAllButton.setEnabled(enabled);
        removeAllButton.setEnabled(enabled);
        getSelectedListControl(parent).setEnabled(enabled);
        if (ordered) {
	        upButton.setEnabled(enabled);
	        downButton.setEnabled(enabled);
        }
    }
    
    private class PropertyChangeListenerImpl implements IPropertyChangeListener {
    
    	@Override
		public void propertyChange(PropertyChangeEvent event) {
			if (event.getProperty().equals(getPreferenceName())) {
				doLoad((String) event.getNewValue());
			}
		}
    	
    }
    
    private class PickableListComparator implements Comparator<String> {
    	
		@Override
		public int compare(String o1, String o2) {
			int i1 = orginalPickableList.indexOf(o1);
			int i2 = orginalPickableList.indexOf(o2);
			return i1 - i2;
		}
		
	}
    
    private class SearchBoxModifyListener implements ModifyListener {
    	@Override
		public void modifyText(ModifyEvent e) {
     		doLoad(selectedItemsList.getItems());
     		selectionChanged();
		}
    }
    
    private class SearchBoxSelectionListener implements SelectionListener {
    	
		@Override
		public void widgetSelected(SelectionEvent e) {
			widgetDefaultSelected(e);
		}
    	
		@Override
		public void widgetDefaultSelected(SelectionEvent e) {
			if (e.detail == SWT.CANCEL) {
				searchCancelled();
			}
		}
    }
    
}
