package codereview.simpleorder.order.rest;

import codereview.simpleorder.order.dto.ItemResponses;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * TODO 테스트 하기 용이하게 변경하기
 * https://www.baeldung.com/spring-boot-running-port
 */

@FeignClient(value = "itemCall", url = "localhost:8080/items")
public interface ItemFeignClient {

    @GetMapping
    ItemResponses requestItems(@RequestParam("ids") List<Long> ids);
}
