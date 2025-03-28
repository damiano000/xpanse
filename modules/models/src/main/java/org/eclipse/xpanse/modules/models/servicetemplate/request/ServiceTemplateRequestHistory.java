/*
 * SPDX-License-Identifier: Apache-2.0
 * SPDX-FileCopyrightText: Huawei Inc.
 */

package org.eclipse.xpanse.modules.models.servicetemplate.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.OffsetDateTimeSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import java.time.OffsetDateTime;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.eclipse.xpanse.modules.models.servicetemplate.request.enums.ServiceTemplateRequestStatus;
import org.eclipse.xpanse.modules.models.servicetemplate.request.enums.ServiceTemplateRequestType;

/** Defines view object for service template request history. */
@EqualsAndHashCode(callSuper = true)
@Data
public class ServiceTemplateRequestHistory extends ServiceTemplateRequestInfo {

    @NotNull
    @Schema(description = "Type of the request.")
    private ServiceTemplateRequestType requestType;

    @NotNull
    @Schema(description = "Status of the request.")
    private ServiceTemplateRequestStatus requestStatus;

    @Schema(description = "Comment of the review request.")
    private String reviewComment;

    @Schema(description = "Status of the request.")
    private Boolean blockTemplateUntilReviewed;

    @NotNull
    @Schema(description = "Create time of the service template request.")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss XXX")
    @JsonSerialize(using = OffsetDateTimeSerializer.class)
    private OffsetDateTime createdTime;

    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss XXX")
    @JsonSerialize(using = OffsetDateTimeSerializer.class)
    @Schema(description = "Last update time of the service template request.")
    private OffsetDateTime lastModifiedTime;
}
