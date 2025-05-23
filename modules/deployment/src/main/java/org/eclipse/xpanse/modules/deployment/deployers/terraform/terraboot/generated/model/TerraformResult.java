/*
 * Terra-Boot API
 * RESTful Services to interact with terraform CLI
 *
 * The version of the OpenAPI document: 1.0.22-SNAPSHOT
 *
 *
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */

package org.eclipse.xpanse.modules.deployment.deployers.terraform.terraboot.generated.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

/** TerraformResult */
@JsonPropertyOrder({
    TerraformResult.JSON_PROPERTY_REQUEST_ID,
    TerraformResult.JSON_PROPERTY_COMMAND_STD_OUTPUT,
    TerraformResult.JSON_PROPERTY_COMMAND_STD_ERROR,
    TerraformResult.JSON_PROPERTY_TERRAFORM_STATE,
    TerraformResult.JSON_PROPERTY_GENERATED_FILE_CONTENT_MAP,
    TerraformResult.JSON_PROPERTY_TERRAFORM_VERSION_USED,
    TerraformResult.JSON_PROPERTY_COMMAND_SUCCESSFUL
})
@jakarta.annotation.Generated(
        value = "org.openapitools.codegen.languages.JavaClientCodegen",
        comments = "Generator version: 7.11.0")
public class TerraformResult {
    public static final String JSON_PROPERTY_REQUEST_ID = "requestId";
    @jakarta.annotation.Nonnull private UUID requestId;

    public static final String JSON_PROPERTY_COMMAND_STD_OUTPUT = "commandStdOutput";
    @jakarta.annotation.Nullable private String commandStdOutput;

    public static final String JSON_PROPERTY_COMMAND_STD_ERROR = "commandStdError";
    @jakarta.annotation.Nullable private String commandStdError;

    public static final String JSON_PROPERTY_TERRAFORM_STATE = "terraformState";
    @jakarta.annotation.Nullable private String terraformState;

    public static final String JSON_PROPERTY_GENERATED_FILE_CONTENT_MAP = "generatedFileContentMap";

    @jakarta.annotation.Nullable
    private Map<String, String> generatedFileContentMap = new HashMap<>();

    public static final String JSON_PROPERTY_TERRAFORM_VERSION_USED = "terraformVersionUsed";
    @jakarta.annotation.Nullable private String terraformVersionUsed;

    public static final String JSON_PROPERTY_COMMAND_SUCCESSFUL = "commandSuccessful";
    @jakarta.annotation.Nullable private Boolean commandSuccessful;

    public TerraformResult() {}

    public TerraformResult requestId(@jakarta.annotation.Nonnull UUID requestId) {

        this.requestId = requestId;
        return this;
    }

    /**
     * Id of the request
     *
     * @return requestId
     */
    @jakarta.annotation.Nonnull
    @JsonProperty(JSON_PROPERTY_REQUEST_ID)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public UUID getRequestId() {
        return requestId;
    }

    @JsonProperty(JSON_PROPERTY_REQUEST_ID)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public void setRequestId(@jakarta.annotation.Nonnull UUID requestId) {
        this.requestId = requestId;
    }

    public TerraformResult commandStdOutput(@jakarta.annotation.Nullable String commandStdOutput) {

        this.commandStdOutput = commandStdOutput;
        return this;
    }

    /**
     * stdout of the command returned as string.
     *
     * @return commandStdOutput
     */
    @jakarta.annotation.Nullable
    @JsonProperty(JSON_PROPERTY_COMMAND_STD_OUTPUT)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public String getCommandStdOutput() {
        return commandStdOutput;
    }

    @JsonProperty(JSON_PROPERTY_COMMAND_STD_OUTPUT)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public void setCommandStdOutput(@jakarta.annotation.Nullable String commandStdOutput) {
        this.commandStdOutput = commandStdOutput;
    }

    public TerraformResult commandStdError(@jakarta.annotation.Nullable String commandStdError) {

        this.commandStdError = commandStdError;
        return this;
    }

    /**
     * stderr of the command returned as string.
     *
     * @return commandStdError
     */
    @jakarta.annotation.Nullable
    @JsonProperty(JSON_PROPERTY_COMMAND_STD_ERROR)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public String getCommandStdError() {
        return commandStdError;
    }

    @JsonProperty(JSON_PROPERTY_COMMAND_STD_ERROR)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public void setCommandStdError(@jakarta.annotation.Nullable String commandStdError) {
        this.commandStdError = commandStdError;
    }

    public TerraformResult terraformState(@jakarta.annotation.Nullable String terraformState) {

        this.terraformState = terraformState;
        return this;
    }

    /**
     * .tfstate file contents returned as string.
     *
     * @return terraformState
     */
    @jakarta.annotation.Nullable
    @JsonProperty(JSON_PROPERTY_TERRAFORM_STATE)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public String getTerraformState() {
        return terraformState;
    }

    @JsonProperty(JSON_PROPERTY_TERRAFORM_STATE)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public void setTerraformState(@jakarta.annotation.Nullable String terraformState) {
        this.terraformState = terraformState;
    }

    public TerraformResult generatedFileContentMap(
            @jakarta.annotation.Nullable Map<String, String> generatedFileContentMap) {

        this.generatedFileContentMap = generatedFileContentMap;
        return this;
    }

    public TerraformResult putGeneratedFileContentMapItem(
            String key, String generatedFileContentMapItem) {
        if (this.generatedFileContentMap == null) {
            this.generatedFileContentMap = new HashMap<>();
        }
        this.generatedFileContentMap.put(key, generatedFileContentMapItem);
        return this;
    }

    /**
     * Data of all other files generated by the terraform execution.The map key contains the file
     * name and value is the file contents as string.
     *
     * @return generatedFileContentMap
     */
    @jakarta.annotation.Nullable
    @JsonProperty(JSON_PROPERTY_GENERATED_FILE_CONTENT_MAP)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public Map<String, String> getGeneratedFileContentMap() {
        return generatedFileContentMap;
    }

    @JsonProperty(JSON_PROPERTY_GENERATED_FILE_CONTENT_MAP)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public void setGeneratedFileContentMap(
            @jakarta.annotation.Nullable Map<String, String> generatedFileContentMap) {
        this.generatedFileContentMap = generatedFileContentMap;
    }

    public TerraformResult terraformVersionUsed(
            @jakarta.annotation.Nullable String terraformVersionUsed) {

        this.terraformVersionUsed = terraformVersionUsed;
        return this;
    }

    /**
     * The version of the Terraform binary used to execute scripts.
     *
     * @return terraformVersionUsed
     */
    @jakarta.annotation.Nullable
    @JsonProperty(JSON_PROPERTY_TERRAFORM_VERSION_USED)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public String getTerraformVersionUsed() {
        return terraformVersionUsed;
    }

    @JsonProperty(JSON_PROPERTY_TERRAFORM_VERSION_USED)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public void setTerraformVersionUsed(@jakarta.annotation.Nullable String terraformVersionUsed) {
        this.terraformVersionUsed = terraformVersionUsed;
    }

    public TerraformResult commandSuccessful(
            @jakarta.annotation.Nullable Boolean commandSuccessful) {

        this.commandSuccessful = commandSuccessful;
        return this;
    }

    /**
     * Get commandSuccessful
     *
     * @return commandSuccessful
     */
    @jakarta.annotation.Nullable
    @JsonProperty(JSON_PROPERTY_COMMAND_SUCCESSFUL)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public Boolean getCommandSuccessful() {
        return commandSuccessful;
    }

    @JsonProperty(JSON_PROPERTY_COMMAND_SUCCESSFUL)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public void setCommandSuccessful(@jakarta.annotation.Nullable Boolean commandSuccessful) {
        this.commandSuccessful = commandSuccessful;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TerraformResult terraformResult = (TerraformResult) o;
        return Objects.equals(this.requestId, terraformResult.requestId)
                && Objects.equals(this.commandStdOutput, terraformResult.commandStdOutput)
                && Objects.equals(this.commandStdError, terraformResult.commandStdError)
                && Objects.equals(this.terraformState, terraformResult.terraformState)
                && Objects.equals(
                        this.generatedFileContentMap, terraformResult.generatedFileContentMap)
                && Objects.equals(this.terraformVersionUsed, terraformResult.terraformVersionUsed)
                && Objects.equals(this.commandSuccessful, terraformResult.commandSuccessful);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                requestId,
                commandStdOutput,
                commandStdError,
                terraformState,
                generatedFileContentMap,
                terraformVersionUsed,
                commandSuccessful);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class TerraformResult {\n");
        sb.append("    requestId: ").append(toIndentedString(requestId)).append("\n");
        sb.append("    commandStdOutput: ").append(toIndentedString(commandStdOutput)).append("\n");
        sb.append("    commandStdError: ").append(toIndentedString(commandStdError)).append("\n");
        sb.append("    terraformState: ").append(toIndentedString(terraformState)).append("\n");
        sb.append("    generatedFileContentMap: ")
                .append(toIndentedString(generatedFileContentMap))
                .append("\n");
        sb.append("    terraformVersionUsed: ")
                .append(toIndentedString(terraformVersionUsed))
                .append("\n");
        sb.append("    commandSuccessful: ")
                .append(toIndentedString(commandSuccessful))
                .append("\n");
        sb.append("}");
        return sb.toString();
    }

    /**
     * Convert the given object to string with each line indented by 4 spaces (except the first
     * line).
     */
    private String toIndentedString(Object o) {
        if (o == null) {
            return "null";
        }
        return o.toString().replace("\n", "\n    ");
    }
}
