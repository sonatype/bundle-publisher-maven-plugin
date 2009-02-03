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
import java.util.List;

import org.apache.maven.artifact.factory.ArtifactFactory;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.artifact.resolver.ArtifactResolver;
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
     * @component
     */
    private ArtifactFactory artifactFactory;

    /**
     * @component
     */
    private ArtifactResolver resolver;

    /**
     * @parameter expression="${localRepository}"
     * @required
     * @readonly
     */
    protected ArtifactRepository localRepository;

    /**
     * @parameter expression="${project.remoteArtifactRepositories}"
     */
    private List<?> remoteRepositories;

    /**
     * File location where targeted Flex SDK is located
     *
     * @parameter expression="${flex.sdk.bundle}"
     * @required
     */
    private File sdkBundle;

    /**
     * File location where targeted Flex SDK is located
     *
     * @parameter expression="${flex.sdk.descriptor}"
     * @required
     */
    private File sdkDescriptor;

    /**
     * Security code to make sure nobody will overwrite FDK version by accident
     *
     * @parameter expression=${overwrite.code}
     */
    private String overwriteCode;

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
            in = new FileInputStream( sdkDescriptor );
            publisher.validate( sdkBundle, in );
            IOUtil.close( in );

            in = new FileInputStream( sdkDescriptor );
            proceed( publisher, sdkBundle, in );
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