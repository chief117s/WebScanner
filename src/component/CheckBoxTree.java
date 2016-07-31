package component;

import javax.swing.tree.DefaultMutableTreeNode;

public class CheckBoxTree extends DefaultMutableTreeNode {
	
	private boolean isSelected;
	
	public CheckBoxTree()
	{
		this(null);
	}

	public CheckBoxTree(Object userObject)
	{
		this(userObject,true,false);
	}
	
	public CheckBoxTree(Object userObject,boolean allowsChildren,boolean isSelected)
	{
		super(userObject,allowsChildren);
		this.isSelected=isSelected;
	}
	
	public boolean isSelected()
	{
		return isSelected;
	}
	
	public void setSelected(boolean _isSelected)
	{
		this.isSelected=_isSelected;
		
		if(_isSelected)
		{
			if(children!=null)
			{
				for(Object obj:children)
				{
					CheckBoxTree node=(CheckBoxTree)obj;
					if(_isSelected!=node.isSelected())
					{
						node.setSelected(_isSelected);
					}
				}
			}
			CheckBoxTree pnode=(CheckBoxTree)parent;
			if(pnode==null)
				return;
			if(pnode.children!=null)
			{
				boolean state=_isSelected;
				for(Object obj:pnode.children)
				{
					CheckBoxTree pnodechild=(CheckBoxTree)obj;
					if(pnodechild.isSelected()!=_isSelected)
					{
						state=pnodechild.isSelected();
						break;
					}
				}
				pnode.setSelected(state);
			}
		}
		
		else if(_isSelected==false)
		{
			if(children!=null)
			{
				boolean state=true;
				for(Object obj:children)
				{
					CheckBoxTree cnode=(CheckBoxTree)obj;
					state=state&&cnode.isSelected();
				}
				if(state==true)
				{
					for(Object obj:children)
					{
						CheckBoxTree cnode=(CheckBoxTree)obj;
						cnode.setSelected(_isSelected);
					}
				}
			}
			CheckBoxTree pnode=(CheckBoxTree) parent;
			if(pnode!=null&&pnode.isSelected()==true)
			{
				pnode.setSelected(_isSelected);
			}
		}
	}
	
	
}
