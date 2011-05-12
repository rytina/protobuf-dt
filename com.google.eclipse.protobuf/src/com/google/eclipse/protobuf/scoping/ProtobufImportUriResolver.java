/*
 * Copyright (c) 2011 Google Inc.
 *
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 *
 * http://www.eclipse.org/legal/epl-v10.html
 */
package com.google.eclipse.protobuf.scoping;

import static com.google.eclipse.protobuf.scoping.IFileUriResolver.PREFIX;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.xtext.scoping.impl.ImportUriResolver;

import com.google.eclipse.protobuf.protobuf.Import;
import com.google.inject.Inject;

/**
 * This implementation mimics how protoc understands URIs of imported files. For example, the URI
 * "platform:/resource/proto1.proto" is understood by EMF but not by protoc. The URI in the proto file needs to be
 * simply "proto1.proto" for protoc to understand it.
 * <p>
 * This {@link ImportUriResolver} adds "platform:/resource" to any URI if is not specified, so EMF can find the
 * imported resource.
 * </p>
 *
 * @author alruiz@google.com (Alex Ruiz)
 */
public class ProtobufImportUriResolver extends ImportUriResolver {

  @Inject private IFileUriResolver delegate;

  /**
   * Prefix used by EMF for resource URIs: "platform:/resource/".
   */
  public static final String URI_PREFIX = PREFIX + "/";

  /**
   * If the given {@code EObject} is a <code>{@link Import}</code>, this method will add "platform:/resource" to the
   * URI of such import if not specified already.
   * @param from the given element to resolve.
   * @return the {@code String} representation of the given object's {@code URI}.
   */
  @Override public String apply(EObject from) {
    if (from instanceof Import) {
      Import anImport = (Import) from;
      String originalUri = anImport.getImportURI();
      resolveImportUri(anImport);
      String applied = super.apply(from);
      anImport.setImportURI(originalUri);
      return applied;
    }
    if (from instanceof Import) resolveImportUri((Import) from);
    return super.apply(from);
  }

  private void resolveImportUri(Import anImport) {
    Resource resource = anImport.eResource();
    String resolved = delegate.resolveUri(anImport.getImportURI(), resource);
    anImport.setImportURI(resolved);
  }
}
