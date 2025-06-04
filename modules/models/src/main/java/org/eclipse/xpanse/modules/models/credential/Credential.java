/*
 * SPDX-License-Identifier: Apache-2.0
 * SPDX-FileCopyrightText: Huawei Inc.
 */

package org.eclipse.xpanse.modules.models.credential;

import jakarta.validation.constraints.NotNull;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.eclipse.xpanse.modules.models.credential.enums.CredentialType;

/** Model to represent a credential received as input. */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Credential {

    @NotNull private CredentialType type;

    @NotNull private Map<String, String> variables;

    /** Get a specific variable by name from the credential. */
    public String get(String key) {
        return variables != null ? variables.get(key) : null;
    }
}
