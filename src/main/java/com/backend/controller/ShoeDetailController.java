package com.backend.controller;

import com.backend.dto.request.ShoeRequest;
import com.backend.service.IShoeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/admin/shoe-detail")
public class ShoeDetailController {
    @Autowired
    private IShoeService iShoeService;

    @GetMapping("/getAllShoeDetailWithPaginate")
//    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> getAllShoeDetailWithPaginate(@RequestParam(defaultValue = "0") Integer page,
                                                          @RequestParam(defaultValue = "2") Integer size){
        return ResponseEntity.ok(iShoeService.getAllShoeItemstest(page,size));
    }
}
