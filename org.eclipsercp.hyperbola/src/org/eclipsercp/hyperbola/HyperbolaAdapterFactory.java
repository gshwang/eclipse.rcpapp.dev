package org.eclipsercp.hyperbola;

import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.model.IWorkbenchAdapter;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.eclipsercp.hyperbola.model.Contact;
import org.eclipsercp.hyperbola.model.ContactsEntry;
import org.eclipsercp.hyperbola.model.ContactsGroup;
import org.eclipsercp.hyperbola.model.Presence;

public class HyperbolaAdapterFactory implements IAdapterFactory{
	
	private IWorkbenchAdapter groupAdapter = new IWorkbenchAdapter() {
		
		@Override
		public Object getParent(Object o) {
			return ((ContactsGroup)o).getParent();
		}
		
		@Override
		public String getLabel(Object o) {
			ContactsGroup group = ((ContactsGroup)o);
			int available = 0;
			Contact[] entries = group.getEntries();
			for(int i=0; i<entries.length; i++)
			{
				Contact contact = entries[i];
				if(contact instanceof ContactsEntry)
				{
					if(((ContactsEntry)contact).getPresence()!=Presence.INVISIBLE)
					{
						available++;
					}
				}
			}
			return group.getName()+" ("+available +"/"+entries.length+") ";
		}
		
		@Override
		public ImageDescriptor getImageDescriptor(Object object) {
			
			return AbstractUIPlugin.imageDescriptorFromPlugin(Application.PLUGIN_ID, IImageKeys.GROUP);
		}
		
		@Override
		public Object[] getChildren(Object o) {
			return ((ContactsGroup)o).getEntries();
		}
	};
	
	private IWorkbenchAdapter entryAdapter = new IWorkbenchAdapter() {
		
		@Override
		public Object getParent(Object o) {
			return ((ContactsEntry)o).getParent();
		}
		
		@Override
		public String getLabel(Object o) {
			ContactsEntry entry = ((ContactsEntry)o);
			return entry.getName()+'-'+entry.getServer();
		}
		
		@Override
		public ImageDescriptor getImageDescriptor(Object object) {
			ContactsEntry entry = ((ContactsEntry)object);
			String key = presenceToKey(entry.getPresence());
			return AbstractUIPlugin.imageDescriptorFromPlugin(Application.PLUGIN_ID, key);
		}
		
		@Override
		public Object[] getChildren(Object o) {
			return new Object[0];
		}
	};

	@Override
	public Object getAdapter(Object adaptableObject, Class adapterType) {
		if(adapterType == IWorkbenchAdapter.class &&
				adaptableObject instanceof ContactsGroup)
			return groupAdapter;
		if(adapterType == IWorkbenchAdapter.class &&
				adaptableObject instanceof ContactsEntry)
			return entryAdapter;
		return null;
	}

	@Override
	public Class[] getAdapterList() {
		return new Class[] {IWorkbenchAdapter.class};
	}
	
	private String presenceToKey(Presence presence)
	{
		if(presence == Presence.ONLINE)
			return IImageKeys.ONLINE;
		if(presence == Presence.AWAY)
			return IImageKeys.AWAY;
		if(presence == Presence.DO_NOT_DISTURB)
			return IImageKeys.DO_NOT_DISTURB;
		if(presence == Presence.INVISIBLE)
			return IImageKeys.OFFLINE;
		return IImageKeys.OFFLINE;
	}

}
