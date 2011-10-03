/*
 * Copyright (c) 2011 Google Inc.
 *
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 *
 * http://www.eclipse.org/legal/epl-v10.html
 */
package com.google.eclipse.protobuf.validation;

import static com.google.eclipse.protobuf.junit.model.find.Name.name;
import static com.google.eclipse.protobuf.junit.model.find.PropertyFinder.findProperty;
import static com.google.eclipse.protobuf.junit.model.find.Root.in;
import static com.google.eclipse.protobuf.protobuf.ProtobufPackage.Literals.FIELD__INDEX;
import static com.google.eclipse.protobuf.validation.ProtobufJavaValidator.INVALID_FIELD_TAG_NUMBER_ERROR;
import static org.eclipse.xtext.validation.ValidationMessageAcceptor.INSIGNIFICANT_INDEX;
import static org.mockito.Mockito.*;

import com.google.eclipse.protobuf.junit.core.XtextRule;
import com.google.eclipse.protobuf.junit.util.MultiLineTextBuilder;
import com.google.eclipse.protobuf.protobuf.*;

import org.eclipse.xtext.validation.ValidationMessageAcceptor;
import org.junit.*;

/**
 * Tests for <code>{@link ProtobufJavaValidator#checkTagNumberIsUnique(Field)}</code>
 * 
 * @author alruiz@google.com (Alex Ruiz)
 */
public class ProtobufJavaValidator_checkTagNumberIsUnique_Test {

  @Rule public XtextRule xtext = XtextRule.unitTestSetup();
  
  private ValidationMessageAcceptor messageAcceptor;
  private ProtobufJavaValidator validator;
  
  @Before public void setUp() {
    messageAcceptor = mock(ValidationMessageAcceptor.class);
    validator = xtext.getInstanceOf(ProtobufJavaValidator.class);
    validator.setMessageAcceptor(messageAcceptor);
  }

  @Test public void should_create_error_if_field_does_not_have_unique_tag_number() {
    MultiLineTextBuilder proto = new MultiLineTextBuilder();
    proto.append("message Person {           ");
    proto.append("  optional long id = 1;    ");
    proto.append("  optional string name = 1;");
    proto.append("}                          ");
    Protobuf root = xtext.parseText(proto);
    Property p = findProperty(name("name"), in(root));
    validator.checkTagNumberIsUnique(p);
    String message = "Field number 1 has already been used in \"Person\" by field \"id\".";
    verify(messageAcceptor).acceptError(message, p, FIELD__INDEX, INSIGNIFICANT_INDEX, INVALID_FIELD_TAG_NUMBER_ERROR);
  }
  
  @Test public void should_not_create_error_if_field_has_unique_tag_number() {
    MultiLineTextBuilder proto = new MultiLineTextBuilder();
    proto.append("message Person {           ");
    proto.append("  optional long id = 1;    ");
    proto.append("  optional string name = 2;");
    proto.append("}                          ");
    Protobuf root = xtext.parseText(proto);
    Property p = findProperty(name("name"), in(root));
    validator.checkTagNumberIsUnique(p);
    verifyZeroInteractions(messageAcceptor);
  }
}