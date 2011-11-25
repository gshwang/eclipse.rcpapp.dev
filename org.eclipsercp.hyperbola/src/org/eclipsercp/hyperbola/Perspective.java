package org.eclipsercp.hyperbola;

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;
import org.eclipsercp.hyperbola.view.ContactsView;

public class Perspective implements IPerspectiveFactory {

	public void createInitialLayout(IPageLayout layout) {
		layout.setEditorAreaVisible(false);
		layout.addStandaloneView(ContactsView.ID, false, IPageLayout.LEFT, 1.0f, layout.getEditorArea());
	}
}
