/*
 * JBoss, Home of Professional Open Source
 * Copyright 2009, Red Hat Middleware LLC, and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.arquillian.container.osgi.arq193;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.io.InputStream;

import javax.inject.Inject;

import org.jboss.arquillian.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.osgi.testing.OSGiManifestBuilder;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.Asset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.osgi.framework.Bundle;

/**
 * [ARQ-193] Create auxillary OSGi test bundle
 *
 * @author thomas.diesler@jboss.com
 * @since 31-Aug-2010
 */
@RunWith(Arquillian.class)
public class ARQ193GeneratedTestCase
{
   @Deployment
   public static Archive<?> createDeployment()
   {
      final JavaArchive archive = ShrinkWrap.create(JavaArchive.class, "empty-bundle");
      archive.setManifest(new Asset()
      {
         public InputStream openStream()
         {
            OSGiManifestBuilder builder = OSGiManifestBuilder.newInstance();
            builder.addBundleSymbolicName(archive.getName());
            builder.addBundleManifestVersion(2);
            return builder.openStream();
         }
      });
      return archive;
   }
   
   @Inject
   public Bundle bundle;
   
   @Test
   public void testBundleInjection() throws Exception
   {
      assertNotNull("Bundle injected", bundle);
      assertEquals("Bundle INSTALLED", Bundle.INSTALLED, bundle.getState());
      
      bundle.start();
      assertEquals("Bundle ACTIVE", Bundle.ACTIVE, bundle.getState());
      
      assertEquals("empty-bundle", bundle.getSymbolicName());
      try
      {
         bundle.loadClass(ARQ193GeneratedTestCase.class.getName());
         fail("ClassNotFoundException expected");
      }
      catch (ClassNotFoundException ex)
      {
         // ignore
      }
      
      bundle.stop();
      assertEquals("Bundle RESOLVED", Bundle.RESOLVED, bundle.getState());
      
      bundle.uninstall();
      assertEquals("Bundle UNINSTALLED", Bundle.UNINSTALLED, bundle.getState());
   }
}
