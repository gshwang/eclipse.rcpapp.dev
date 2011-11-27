/*******************************************************************************
 * Copyright (c) 2010 Jean-Michel Lemieux, Jeff McAffer, Chris Aniszczyk and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Hyperbola is an RCP application developed for the book
 *     Eclipse Rich Client Platform - 
 *         Designing, Coding, and Packaging Java Applications
 * See http://eclipsercp.org
 *
 * Contributors:
 *     Jean-Michel Lemieux and Jeff McAffer - initial API and implementation
 *     Chris Aniszczyk - edits for the second edition
 *******************************************************************************/
package org.eclipsercp.hyperbola;

import org.eclipse.jface.action.IStatusLineManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TrayItem;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;
import org.eclipse.ui.plugin.AbstractUIPlugin;

public class ApplicationWorkbenchWindowAdvisor extends WorkbenchWindowAdvisor {
	
	private Image statusImage;
	private TrayItem trayItem;
	
	private ApplicationActionBarAdvisor actionBarAdvisor;
	

	public ApplicationWorkbenchWindowAdvisor(
			IWorkbenchWindowConfigurer configurer) {
		super(configurer);
	}

	public ActionBarAdvisor createActionBarAdvisor(
			IActionBarConfigurer configurer) {
		actionBarAdvisor = new ApplicationActionBarAdvisor(configurer); 
		return actionBarAdvisor;
	}

	public void preWindowOpen() {
		IWorkbenchWindowConfigurer configurer = getWindowConfigurer();
		configurer.setInitialSize(new Point(250, 300));
		configurer.setShowCoolBar(true);
		configurer.setShowMenuBar(true);
		configurer.setShowStatusLine(true);
		configurer.setTitle("Hyperbola");
	}
	
	@Override
	public void postWindowOpen() {
		statusImage = AbstractUIPlugin.imageDescriptorFromPlugin(
				"org.eclipsercp.hyperbola", IImageKeys.ONLINE).createImage();
		IStatusLineManager statusline = getWindowConfigurer().getActionBarConfigurer().getStatusLineManager();
		statusline.setMessage(statusImage, "OnLine");
		final IWorkbenchWindow window = getWindowConfigurer().getWindow();
		trayItem = initTaskItem(window);
		if(trayItem != null)
		{
			hookPopupMenu(window);
			hookMinimize(window);
		}
	}
	
	private void hookMinimize(final IWorkbenchWindow window) {
		window.getShell().addShellListener(new ShellAdapter() {
			@Override
			public void shellIconified(ShellEvent e) {
				window.getShell().setVisible(false);
			}
		});
		trayItem.addListener(SWT.DefaultSelection, new Listener() {
			
			@Override
			public void handleEvent(Event event) {
				Shell shell = window.getShell();
				if(!shell.isVisible())
				{
					shell.setVisible(true);
					window.getShell().setMinimized(false);
				}
			}
		});
	}

	private void hookPopupMenu(final IWorkbenchWindow window) {
		//
		trayItem.addListener(SWT.MenuDetect, new Listener() {
			
			@Override
			public void handleEvent(Event event) {
				MenuManager trayMenu = new MenuManager();
				Menu menu = trayMenu.createContextMenu(window.getShell());
			}
		});
	}

	private TrayItem initTaskItem(IWorkbenchWindow window) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void dispose() {
		statusImage.dispose();
	}
}
