/*
 * Tofu-Maker API
 * RESTful Services to interact with opentofu CLI
 *
 * The version of the OpenAPI document: 1.0.13-SNAPSHOT
 *
 *
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */

package org.eclipse.xpanse.modules.deployment.deployers.opentofu.tofumaker.generated.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

/** OpenTofuAsyncDestroyFromScriptsRequest */
@JsonPropertyOrder({
    OpenTofuAsyncDestroyFromScriptsRequest.JSON_PROPERTY_REQUEST_ID,
    OpenTofuAsyncDestroyFromScriptsRequest.JSON_PROPERTY_OPEN_TOFU_VERSION,
    OpenTofuAsyncDestroyFromScriptsRequest.JSON_PROPERTY_VARIABLES,
    OpenTofuAsyncDestroyFromScriptsRequest.JSON_PROPERTY_ENV_VARIABLES,
    OpenTofuAsyncDestroyFromScriptsRequest.JSON_PROPERTY_SCRIPT_FILES,
    OpenTofuAsyncDestroyFromScriptsRequest.JSON_PROPERTY_TF_STATE,
    OpenTofuAsyncDestroyFromScriptsRequest.JSON_PROPERTY_WEBHOOK_CONFIG
})
@jakarta.annotation.Generated(
        value = "org.openapitools.codegen.languages.JavaClientCodegen",
        comments = "Generator version: 7.11.0")
public class OpenTofuAsyncDestroyFromScriptsRequest {
    public static final String JSON_PROPERTY_REQUEST_ID = "requestId";
    @jakarta.annotation.Nullable private UUID requestId;

    public static final String JSON_PROPERTY_OPEN_TOFU_VERSION = "openTofuVersion";
    @jakarta.annotation.Nonnull private String openTofuVersion;

    public static final String JSON_PROPERTY_VARIABLES = "variables";
    @jakarta.annotation.Nonnull private Map<String, Object> variables = new HashMap<>();

    public static final String JSON_PROPERTY_ENV_VARIABLES = "envVariables";
    @jakarta.annotation.Nullable private Map<String, String> envVariables = new HashMap<>();

    public static final String JSON_PROPERTY_SCRIPT_FILES = "scriptFiles";
    @jakarta.annotation.Nonnull private Map<String, String> scriptFiles = new HashMap<>();

    public static final String JSON_PROPERTY_TF_STATE = "tfState";
    @jakarta.annotation.Nonnull private String tfState;

    public static final String JSON_PROPERTY_WEBHOOK_CONFIG = "webhookConfig";
    @jakarta.annotation.Nonnull private WebhookConfig webhookConfig;

    public OpenTofuAsyncDestroyFromScriptsRequest() {}

    public OpenTofuAsyncDestroyFromScriptsRequest requestId(
            @jakarta.annotation.Nullable UUID requestId) {

        this.requestId = requestId;
        return this;
    }

    /**
     * Id of the request.
     *
     * @return requestId
     */
    @jakarta.annotation.Nullable
    @JsonProperty(JSON_PROPERTY_REQUEST_ID)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public UUID getRequestId() {
        return requestId;
    }

    @JsonProperty(JSON_PROPERTY_REQUEST_ID)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public void setRequestId(@jakarta.annotation.Nullable UUID requestId) {
        this.requestId = requestId;
    }

    public OpenTofuAsyncDestroyFromScriptsRequest openTofuVersion(
            @jakarta.annotation.Nonnull String openTofuVersion) {

        this.openTofuVersion = openTofuVersion;
        return this;
    }

    /**
     * The required version of the OpenTofu which will execute the scripts.
     *
     * @return openTofuVersion
     */
    @jakarta.annotation.Nonnull
    @JsonProperty(JSON_PROPERTY_OPEN_TOFU_VERSION)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public String getOpenTofuVersion() {
        return openTofuVersion;
    }

    @JsonProperty(JSON_PROPERTY_OPEN_TOFU_VERSION)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public void setOpenTofuVersion(@jakarta.annotation.Nonnull String openTofuVersion) {
        this.openTofuVersion = openTofuVersion;
    }

    public OpenTofuAsyncDestroyFromScriptsRequest variables(
            @jakarta.annotation.Nonnull Map<String, Object> variables) {

        this.variables = variables;
        return this;
    }

    public OpenTofuAsyncDestroyFromScriptsRequest putVariablesItem(
            String key, Object variablesItem) {
        this.variables.put(key, variablesItem);
        return this;
    }

    /**
     * Key-value pairs of regular variables that must be used to execute the OpenTofu request.
     *
     * @return variables
     */
    @jakarta.annotation.Nonnull
    @JsonProperty(JSON_PROPERTY_VARIABLES)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public Map<String, Object> getVariables() {
        return variables;
    }

    @JsonProperty(JSON_PROPERTY_VARIABLES)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public void setVariables(@jakarta.annotation.Nonnull Map<String, Object> variables) {
        this.variables = variables;
    }

    public OpenTofuAsyncDestroyFromScriptsRequest envVariables(
            @jakarta.annotation.Nullable Map<String, String> envVariables) {

        this.envVariables = envVariables;
        return this;
    }

    public OpenTofuAsyncDestroyFromScriptsRequest putEnvVariablesItem(
            String key, String envVariablesItem) {
        if (this.envVariables == null) {
            this.envVariables = new HashMap<>();
        }
        this.envVariables.put(key, envVariablesItem);
        return this;
    }

    /**
     * Key-value pairs of variables that must be injected as environment variables to OpenTofu
     * process.
     *
     * @return envVariables
     */
    @jakarta.annotation.Nullable
    @JsonProperty(JSON_PROPERTY_ENV_VARIABLES)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public Map<String, String> getEnvVariables() {
        return envVariables;
    }

    @JsonProperty(JSON_PROPERTY_ENV_VARIABLES)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public void setEnvVariables(@jakarta.annotation.Nullable Map<String, String> envVariables) {
        this.envVariables = envVariables;
    }

    public OpenTofuAsyncDestroyFromScriptsRequest scriptFiles(
            @jakarta.annotation.Nonnull Map<String, String> scriptFiles) {

        this.scriptFiles = scriptFiles;
        return this;
    }

    public OpenTofuAsyncDestroyFromScriptsRequest putScriptFilesItem(
            String key, String scriptFilesItem) {
        this.scriptFiles.put(key, scriptFilesItem);
        return this;
    }

    /**
     * Map stores file name and content of all script files for destroy request
     *
     * @return scriptFiles
     */
    @jakarta.annotation.Nonnull
    @JsonProperty(JSON_PROPERTY_SCRIPT_FILES)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public Map<String, String> getScriptFiles() {
        return scriptFiles;
    }

    @JsonProperty(JSON_PROPERTY_SCRIPT_FILES)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public void setScriptFiles(@jakarta.annotation.Nonnull Map<String, String> scriptFiles) {
        this.scriptFiles = scriptFiles;
    }

    public OpenTofuAsyncDestroyFromScriptsRequest tfState(
            @jakarta.annotation.Nonnull String tfState) {

        this.tfState = tfState;
        return this;
    }

    /**
     * The .tfState file content after deployment
     *
     * @return tfState
     */
    @jakarta.annotation.Nonnull
    @JsonProperty(JSON_PROPERTY_TF_STATE)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public String getTfState() {
        return tfState;
    }

    @JsonProperty(JSON_PROPERTY_TF_STATE)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public void setTfState(@jakarta.annotation.Nonnull String tfState) {
        this.tfState = tfState;
    }

    public OpenTofuAsyncDestroyFromScriptsRequest webhookConfig(
            @jakarta.annotation.Nonnull WebhookConfig webhookConfig) {

        this.webhookConfig = webhookConfig;
        return this;
    }

    /**
     * Configuration information of webhook.
     *
     * @return webhookConfig
     */
    @jakarta.annotation.Nonnull
    @JsonProperty(JSON_PROPERTY_WEBHOOK_CONFIG)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public WebhookConfig getWebhookConfig() {
        return webhookConfig;
    }

    @JsonProperty(JSON_PROPERTY_WEBHOOK_CONFIG)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public void setWebhookConfig(@jakarta.annotation.Nonnull WebhookConfig webhookConfig) {
        this.webhookConfig = webhookConfig;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        OpenTofuAsyncDestroyFromScriptsRequest openTofuAsyncDestroyFromScriptsRequest =
                (OpenTofuAsyncDestroyFromScriptsRequest) o;
        return Objects.equals(this.requestId, openTofuAsyncDestroyFromScriptsRequest.requestId)
                && Objects.equals(
                        this.openTofuVersion,
                        openTofuAsyncDestroyFromScriptsRequest.openTofuVersion)
                && Objects.equals(this.variables, openTofuAsyncDestroyFromScriptsRequest.variables)
                && Objects.equals(
                        this.envVariables, openTofuAsyncDestroyFromScriptsRequest.envVariables)
                && Objects.equals(
                        this.scriptFiles, openTofuAsyncDestroyFromScriptsRequest.scriptFiles)
                && Objects.equals(this.tfState, openTofuAsyncDestroyFromScriptsRequest.tfState)
                && Objects.equals(
                        this.webhookConfig, openTofuAsyncDestroyFromScriptsRequest.webhookConfig);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                requestId,
                openTofuVersion,
                variables,
                envVariables,
                scriptFiles,
                tfState,
                webhookConfig);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class OpenTofuAsyncDestroyFromScriptsRequest {\n");
        sb.append("    requestId: ").append(toIndentedString(requestId)).append("\n");
        sb.append("    openTofuVersion: ").append(toIndentedString(openTofuVersion)).append("\n");
        sb.append("    variables: ").append(toIndentedString(variables)).append("\n");
        sb.append("    envVariables: ").append(toIndentedString(envVariables)).append("\n");
        sb.append("    scriptFiles: ").append(toIndentedString(scriptFiles)).append("\n");
        sb.append("    tfState: ").append(toIndentedString(tfState)).append("\n");
        sb.append("    webhookConfig: ").append(toIndentedString(webhookConfig)).append("\n");
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
