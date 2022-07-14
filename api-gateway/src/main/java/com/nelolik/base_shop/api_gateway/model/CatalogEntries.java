package com.nelolik.base_shop.api_gateway.model;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class CatalogEntries {

    private List<String> categories;

}
