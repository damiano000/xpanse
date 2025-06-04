/*
 * SPDX-License-Identifier: Apache-2.0
 * SPDX-FileCopyrightText: Huawei Inc.
 */

package org.eclipse.xpanse.modules.models.credential.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.xpanse.modules.models.common.exceptions.UnsupportedEnumValueException;

/** The credential types. */
public enum CredentialType {
    VARIABLES("variables"),
    HTTP_AUTHENTICATION("http_authentication"),
    API_KEY("api_key"),
    OAUTH2("oauth2"),
    USERNAME_PASSWORD("username_password"),
    AK_SK("ak_sk");

    private final String type;

    CredentialType(String type) {
        this.type = type;
    }

    /** For CredentialType serialize. */
    @JsonCreator
    public static CredentialType getByValue(String type) {
        for (CredentialType credentialType : values()) {
            if (credentialType.type.equals(StringUtils.lowerCase(type))) {
                return credentialType;
            }
        }
        throw new UnsupportedEnumValueException(
                String.format("CredentialType value %s is not supported.", type));
    }

    /** For CredentialType deserialize. */
    @JsonValue
    public String toValue() {
        return this.type;
    }
}
