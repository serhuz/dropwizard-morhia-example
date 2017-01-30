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

package xyz.randomcode.dropwizard_morphia.example.api;

import io.dropwizard.jersey.params.NonEmptyStringParam;
import io.dropwizard.validation.Validated;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.bson.types.ObjectId;
import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.valuehandling.UnwrapValidatedValue;
import org.mongodb.morphia.Key;
import xyz.randomcode.dropwizard_morphia.example.db.ExampleEntity;
import xyz.randomcode.dropwizard_morphia.example.db.ExampleEntityDAO;
import xyz.randomcode.dropwizard_morphia.example.resources.IdResponse;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

@Api
@Path("/entity")
public class ExampleResource {

    private ExampleEntityDAO dao;

    @Inject
    public ExampleResource(ExampleEntityDAO dao) {
        this.dao = dao;
    }

    @ApiOperation("Save entity")
    @Path("/save")
    @POST
    public IdResponse saveEntity(@Valid @NotNull ExampleEntity entity) {
        Key<ExampleEntity> key = dao.save(entity);
        return new IdResponse(key.getId().toString());
    }

    @ApiOperation("Load entity")
    @Path("/load/{id}")
    @GET
    public ExampleEntity loadEntity(@PathParam("id") @Validated @UnwrapValidatedValue @NotEmpty NonEmptyStringParam id) {
        return dao.get(new ObjectId(id.get().get()));
    }
}
