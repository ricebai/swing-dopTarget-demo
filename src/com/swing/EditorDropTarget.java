package com.swing;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import javax.swing.JEditorPane;

public class EditorDropTarget implements DropTargetListener {

	protected JEditorPane pane;
	protected DropTarget dropTarget;
	protected boolean acceptableType; // Indicates whether data is acceptable
	
	public EditorDropTarget(JEditorPane pane) {
		this.pane = pane;

		// Create the DropTarget and register
		// it with the JEditorPane.
		dropTarget = new DropTarget(pane, DnDConstants.ACTION_COPY_OR_MOVE, this, true, null);
	}

	// Implementation of the DropTargetListener interface
	public void dragEnter(DropTargetDragEvent dtde) {
		DnDUtils.debugPrintln("dragEnter, drop action = " + DnDUtils.showActions(dtde.getDropAction()));

		// Get the type of object being transferred and determine
		// whether it is appropriate.
		checkTransferType(dtde);

		// Accept or reject the drag.
		acceptOrRejectDrag(dtde);
	}

	public void dragExit(DropTargetEvent dte) {
		DnDUtils.debugPrintln("DropTarget dragExit");
	}

	public void dragOver(DropTargetDragEvent dtde) {
		DnDUtils.debugPrintln("DropTarget dragOver, drop action = " + DnDUtils.showActions(dtde.getDropAction()));

		// Accept or reject the drag
		acceptOrRejectDrag(dtde);
	}

	public void dropActionChanged(DropTargetDragEvent dtde) {
		DnDUtils.debugPrintln(
				"DropTarget dropActionChanged, drop action = " + DnDUtils.showActions(dtde.getDropAction()));

		// Accept or reject the drag
		acceptOrRejectDrag(dtde);
	}

	public void drop(DropTargetDropEvent dtde) {
		DnDUtils.debugPrintln("DropTarget drop, drop action = " + DnDUtils.showActions(dtde.getDropAction()));

		// Check the drop action
		if ((dtde.getDropAction() & DnDConstants.ACTION_COPY_OR_MOVE) != 0) {
			// Accept the drop and get the transfer data
			dtde.acceptDrop(dtde.getDropAction());
			Transferable transferable = dtde.getTransferable();

			try {
				boolean result = dropFile(transferable);

				dtde.dropComplete(result);
				DnDUtils.debugPrintln("Drop completed, success: " + result);
			} catch (Exception e) {
				DnDUtils.debugPrintln("Exception while handling drop " + e);
				dtde.dropComplete(false);
			}
		} else {
			DnDUtils.debugPrintln("Drop target rejected drop");
			dtde.rejectDrop();
		}
	}

	// Internal methods start here

	protected boolean acceptOrRejectDrag(DropTargetDragEvent dtde) {
		int dropAction = dtde.getDropAction();
		int sourceActions = dtde.getSourceActions();
		boolean acceptedDrag = false;

		DnDUtils.debugPrintln("/tSource actions are " + DnDUtils.showActions(sourceActions) + ", drop action is "
				+ DnDUtils.showActions(dropAction));

		// Reject if the object being transferred
		// or the operations available are not acceptable.
		if (!acceptableType || (sourceActions & DnDConstants.ACTION_COPY_OR_MOVE) == 0) {
			DnDUtils.debugPrintln("Drop target rejecting drag");
			dtde.rejectDrag();
		} else if ((dropAction & DnDConstants.ACTION_COPY_OR_MOVE) == 0) {
			// Not offering copy or move - suggest a copy
			DnDUtils.debugPrintln("Drop target offering COPY");
			dtde.acceptDrag(DnDConstants.ACTION_COPY);
			acceptedDrag = true;
		} else {
			// Offering an acceptable operation: accept
			DnDUtils.debugPrintln("Drop target accepting drag");
			dtde.acceptDrag(dropAction);
			acceptedDrag = true;
		}

		return acceptedDrag;
	}

	protected void checkTransferType(DropTargetDragEvent dtde) {
		// Only accept a list of files
		acceptableType = dtde.isDataFlavorSupported(DataFlavor.javaFileListFlavor);

		DnDUtils.debugPrintln("File type acceptable - " + acceptableType);
	}

	// This method handles a drop for a list of files
	@SuppressWarnings("deprecation")
	protected boolean dropFile(Transferable transferable)
			throws IOException, UnsupportedFlavorException, MalformedURLException {
		
		// 获得拖拽文件列表
		List<?> fileList = (List<?>) transferable.getTransferData(DataFlavor.javaFileListFlavor);
		
		// 获得第一个文件
		File transferFile = (File) fileList.get(0);
		System.out.println("EditorDropTarget.dropFile:"+transferFile);
		final URL transferURL = transferFile.toURL();
		DnDUtils.debugPrintln("File URL is " + transferURL);

		// 显示内容
		pane.setPage(transferURL);

		return true;
	}
}
