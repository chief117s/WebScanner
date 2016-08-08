package view;

import java.util.Vector;

import javax.swing.JTable;

public class JTableResultTable extends JTable {
	
	public JTableResultTable(Vector<Vector<String>> result,Vector<String> headlist)
	{
		super(result, headlist);
		this.setAutoCreateRowSorter(true);
	}
}
