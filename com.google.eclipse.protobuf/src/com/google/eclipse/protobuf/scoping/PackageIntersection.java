/*
 * Copyright (c) 2011 Google Inc.
 *
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 *
 * http://www.eclipse.org/legal/epl-v10.html
 */
package com.google.eclipse.protobuf.scoping;

import static java.util.Collections.*;

import com.google.eclipse.protobuf.protobuf.Package;
import com.google.inject.Inject;

import org.eclipse.xtext.naming.*;

import java.util.*;

/**
 * @author alruiz@google.com (Alex Ruiz)
 */
class PackageIntersection {

  @Inject private final IQualifiedNameConverter converter = new IQualifiedNameConverter.DefaultImpl();

  List<String> intersection(Package p1, Package p2) {
    if (p2 == null) return null;
    return intersection(converter.toQualifiedName(p1.getName()), converter.toQualifiedName(p2.getName()));
  }

  private List<String> intersection(QualifiedName n1, QualifiedName n2) {
    return intersection(n1.getSegments(), n2.getSegments());
  }

  private List<String> intersection(List<String> n1, List<String> n2) {
    List<String> intersection = new ArrayList<String>();
    int n1Count = n1.size();
    int n2Count = n2.size();
    boolean differenceFound = false;
    for (int i = 0; (i < n1Count && i < n2Count); i++) {
      String n2Segment = n2.get(i);
      if (differenceFound || !n1.get(i).equals(n2Segment)) {
        differenceFound = true;
        intersection.add(n2Segment);
      }
    }
    if (intersection.equals(n2)) return emptyList();
    return unmodifiableList(intersection);
  }

}