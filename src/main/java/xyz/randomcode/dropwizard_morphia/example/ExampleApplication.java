/*
 * Copyright 2017 Sergei Munovarov
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package xyz.randomcode.dropwizard_morphia.example;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.federecio.dropwizard.swagger.SwaggerBundle;
import io.federecio.dropwizard.swagger.SwaggerBundleConfiguration;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.mongodb.morphia.Datastore;
import xyz.randomcode.dropwizard_morphia.MongoConfiguration;
import xyz.randomcode.dropwizard_morphia.MorphiaBundle;
import xyz.randomcode.dropwizard_morphia.example.api.ExampleResource;
import xyz.randomcode.dropwizard_morphia.example.db.ExampleEntity;
import xyz.randomcode.dropwizard_morphia.example.db.ExampleEntityDAO;

public class ExampleApplication extends Application<ExampleConfiguration> {

    private MorphiaBundle<ExampleConfiguration> morphiaBundle =
            new MorphiaBundle<ExampleConfiguration>(ExampleEntity.class) {
                @Override
                protected MongoConfiguration getMongo(ExampleConfiguration configuration) {
                    return configuration.getMongo();
                }
            };

    public static void main(String[] args) throws Exception {
        new ExampleApplication().run(args);
    }

    @Override
    public void initialize(Bootstrap<ExampleConfiguration> bootstrap) {
        bootstrap.addBundle(morphiaBundle);
        bootstrap.addBundle(new SwaggerBundle<ExampleConfiguration>() {
            @Override
            protected SwaggerBundleConfiguration getSwaggerBundleConfiguration(ExampleConfiguration configuration) {
                return configuration.getSwaggerBundleConfiguration();
            }
        });
    }

    @Override
    public void run(ExampleConfiguration configuration, Environment environment) throws Exception {
        Datastore datastore = morphiaBundle.getDatastore();
        ExampleEntityDAO dao = new ExampleEntityDAO(datastore);

        environment.jersey().register(new AbstractBinder() {
            @Override
            protected void configure() {
                bind(dao).to(ExampleEntityDAO.class);
            }
        });
        environment.jersey().register(ExampleResource.class);
    }
}
