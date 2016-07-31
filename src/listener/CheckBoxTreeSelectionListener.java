package listener;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JTree;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import component.CheckBoxTree;

public class CheckBoxTreeSelectionListener extends MouseAdapter {
	
	@Override
	public void mouseClicked(MouseEvent e)
	{
		JTree tree=(JTree)e.getSource();
		int x=e.getX(),y=e.getY();
		int row=tree.getRowForLocation(x, y);
		TreePath path=tree.getPathForRow(row);
		if(path!=null)
		{
			CheckBoxTree node=(CheckBoxTree)path.getLastPathComponent();
			if(node!=null)
			{
				boolean isSelected=!node.isSelected();
				node.setSelected(isSelected);
				((DefaultTreeModel)tree.getModel()).nodeStructureChanged(node);
			}
		}
	}

}
