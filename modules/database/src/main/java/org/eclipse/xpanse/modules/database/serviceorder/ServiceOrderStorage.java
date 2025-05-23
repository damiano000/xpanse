/*
 * SPDX-License-Identifier: Apache-2.0
 * SPDX-FileCopyrightText: Huawei Inc.
 *
 */

package org.eclipse.xpanse.modules.database.serviceorder;

import java.util.List;
import java.util.UUID;

/** Interface for persist of ServiceModificationAudit. */
public interface ServiceOrderStorage {

    /**
     * Add or update service order data to database.
     *
     * @param entity the data of service order.
     * @return Returns the stored database entry.
     */
    ServiceOrderEntity storeAndFlush(ServiceOrderEntity entity);

    /**
     * Method to list database entry based ServiceOrderEntity.
     *
     * @param query query model for search service order entity.
     * @return Returns the database entry for the provided arguments.
     */
    List<ServiceOrderEntity> queryEntities(ServiceOrderEntity query);

    /**
     * Method to get database entry based ServiceOrderEntity.
     *
     * @param orderId orderId of ServiceOrderEntity.
     * @return Returns the database entry for the provided arguments.
     */
    ServiceOrderEntity getEntityById(UUID orderId);

    /**
     * Method to get database entry based ServiceOrderEntity.
     *
     * @param orderIds orderIds of ServiceOrderEntity.
     * @return Returns the database entry for the provided arguments.
     */
    List<ServiceOrderEntity> getEntitiesByIds(List<UUID> orderIds);

    /**
     * Delete service order entity from database by entity.
     *
     * @param entity service order entity
     */
    void delete(ServiceOrderEntity entity);

    /**
     * Batch delete service order entities from database.
     *
     * @param entities service order entities
     */
    void deleteBatch(List<ServiceOrderEntity> entities);
}
