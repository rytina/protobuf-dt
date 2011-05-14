/*
 * Copyright (c) 2011 Google Inc.
 *
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 *
 * http://www.eclipse.org/legal/epl-v10.html
 */
package com.google.eclipse.protobuf.ui.util;

import static org.eclipse.core.resources.IResource.FOLDER;

import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IStatus;

import com.google.inject.Singleton;

/**
 * Validates names of folders.
 * 
 * @author alruiz@google.com (Alex Ruiz)
 */
@Singleton
public class FolderNameValidator {

  public IStatus validateFolderName(String folderName) {
    IWorkspace workspace = ResourcesPlugin.getWorkspace();
    return workspace.validateName(folderName, FOLDER);
  }
}