/*
 * SPDX-License-Identifier: Apache-2.0
 * SPDX-FileCopyrightText: Huawei Inc.
 */

package org.eclipse.xpanse.modules.models.service.deploy;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Map;
import lombok.Data;
import org.eclipse.xpanse.modules.models.common.enums.Category;
import org.eclipse.xpanse.modules.models.common.enums.Csp;
import org.eclipse.xpanse.modules.models.servicetemplate.Ocl;
import org.eclipse.xpanse.modules.models.servicetemplate.enums.ServiceHostingType;

/**
 * Request body for service deployment.
 */
@Data
public class DeployRequestBase implements Serializable {
    /**
     * The id of the user who ordered the Service.
     */
    @Hidden
    private String userId;

    /**
     * The category of the Service.
     */
    @NotNull
    @Schema(description = "The category of the service")
    private Category category;

    /**
     * The name of the Service ordered.
     */
    @NotNull
    @NotBlank
    @NotEmpty
    @Schema(description = "The name of the service ordered.")
    private String serviceName;

    /**
     * Customer provided name for the service.
     */
    @Schema(description = "Customer's name for the service. Used only for customer's reference."
            + "If not provided, this value will be auto-generated")
    private String customerServiceName;

    /**
     * The version of the Service.
     */
    @NotNull
    @NotBlank
    @NotEmpty
    @Schema(description = "The version of service")
    private String version;


    @NotNull
    @NotBlank
    @NotEmpty
    @Schema(description = "The region of the provider.")
    private String region;

    /**
     * The csp of the Service.
     */
    @NotNull
    @Schema(description = "The csp of the Service.")
    private Csp csp;

    /**
     * The flavor of the Service.
     */
    @NotNull
    @NotBlank
    @NotEmpty
    @Schema(description = "The flavor of the Service.")
    private String flavor;

    @NotNull
    @Schema(description = "Defines which cloud service account is used "
            + "for deploying cloud resources.")
    private ServiceHostingType serviceHostingType;

    @Hidden
    private Ocl ocl;

    /**
     * The property of the Service.
     */
    @Schema(description = "The properties for the requested service")
    private Map<String, Object> serviceRequestProperties;
}
