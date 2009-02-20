/**
 *  Copyright 2008 Marvin Herman Froeder
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *  http://www.apache.org/licenses/LICENSE-2.0
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 *
 */
package org.sonatype.maven.plugin.bundlepublisher;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.codehaus.plexus.util.IOUtil;
import org.sonatype.plexus.component.bundlepublisher.BundlePublisher;
import org.sonatype.plexus.component.bundlepublisher.PublishingException;

public abstract class AbstractBundlePublisherMojo
    extends AbstractMojo
{

    /**
     * @component
     */
    private BundlePublisher publisher;

    /**
     * @parameter expression="${localRepository}"
     * @required
     * @readonly
     */
    protected ArtifactRepository localRepository;

    /**
     * File location where targeted Flex SDK is located
     * 
     * @parameter expression="${bundle}"
     * @required
     */
    private File bundle;

    /**
     * File location where targeted Flex SDK is located
     * 
     * @parameter expression="${descriptor}"
     * @required
     */
    private File descriptor;

    public AbstractBundlePublisherMojo()
    {
        super();
    }

    public final void execute()
        throws MojoExecutionException, MojoFailureException
    {

        InputStream in = null;
        try
        {
            in = new FileInputStream( descriptor );
            publisher.validate( bundle, in );
            IOUtil.close( in );

            in = new FileInputStream( descriptor );
            proceed( publisher, bundle, in );
            IOUtil.close( in );
        }
        catch ( PublishingException e )
        {
            throw new MojoFailureException( "Unable to install flex SDK: " + e.getMessage(), e );
        }
        catch ( FileNotFoundException e )
        {
            throw new MojoFailureException( "Flex SDK descriptor not found", e );
        }
        finally
        {
            IOUtil.close( in );
        }
    }

    protected abstract void proceed( BundlePublisher publisher, File sdkBundle, InputStream sdkDescriptor )
        throws PublishingException;

}