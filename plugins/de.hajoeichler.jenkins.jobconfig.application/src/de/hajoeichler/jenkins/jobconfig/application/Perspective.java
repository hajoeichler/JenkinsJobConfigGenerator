package de.hajoeichler.jenkins.jobconfig.application;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;
import org.eclipse.ui.progress.IProgressConstants;

public class Perspective implements IPerspectiveFactory {

	public void createInitialLayout(IPageLayout layout) {
		String editorArea = layout.getEditorArea();

		IFolderLayout leftFolder = layout.createFolder(
				"left", IPageLayout.LEFT, (float) 0.30, editorArea); //$NON-NLS-1$
		leftFolder.addView(IPageLayout.ID_PROJECT_EXPLORER);

		IFolderLayout bottomFolder = layout.createFolder(
				"bottom", IPageLayout.BOTTOM, (float) 0.65, editorArea); //$NON-NLS-1$
		bottomFolder.addView(IPageLayout.ID_PROBLEM_VIEW);
		bottomFolder.addView(IPageLayout.ID_TASK_LIST);
		bottomFolder.addPlaceholder(IPageLayout.ID_BOOKMARKS);
		bottomFolder.addPlaceholder(IProgressConstants.PROGRESS_VIEW_ID);

		IFolderLayout rightFolder = layout.createFolder(
				"right", IPageLayout.RIGHT, (float) 0.65, editorArea); //$NON-NLS-1$
		rightFolder.addView(IPageLayout.ID_OUTLINE);

		layout.addActionSet(IPageLayout.ID_NAVIGATE_ACTION_SET);

		// views - standard workbench
		layout.addShowViewShortcut(IPageLayout.ID_OUTLINE);
		layout.addShowViewShortcut(IPageLayout.ID_PROBLEM_VIEW);
		layout.addShowViewShortcut(IPageLayout.ID_TASK_LIST);
		layout.addShowViewShortcut(IProgressConstants.PROGRESS_VIEW_ID);

		layout.addNewWizardShortcut("org.eclipse.ui.wizards.new.folder");//$NON-NLS-1$
		layout.addNewWizardShortcut("org.eclipse.ui.wizards.new.file");//$NON-NLS-1$
		layout.addNewWizardShortcut("org.eclipse.ui.editors.wizards.UntitledTextFileWizard");//$NON-NLS-1$	
	}
}
