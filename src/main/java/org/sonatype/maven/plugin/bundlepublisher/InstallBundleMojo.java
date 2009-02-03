/**
 *  Copyright 2008 Marvin Herman Froeder
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *  http://www.apache.org/licenses/LICENSE-2.0
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 *
 */
package org.sonatype.maven.plugin.bundlepublisher;

import java.io.File;
import java.io.InputStream;

import org.sonatype.plexus.component.bundlepublisher.BundlePublisher;
import org.sonatype.plexus.component.bundlepublisher.PublishingException;

/**
 * @goal install-bundle
 * @requiresProject false
 * @requiresDirectInvocation true
 * @author marvin
 */
public class InstallBundleMojo
    extends AbstractBundlePublisherMojo
{

    @Override
    protected void proceed( BundlePublisher publisher, File sdkBundle, InputStream sdkDescriptor )
        throws PublishingException
    {
        publisher.install( sdkBundle, sdkDescriptor, localRepository );
    }
}
