package com.lucky.smartadplatform.application.rest.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import com.lucky.smartadplatform.application.rest.model.GenericResponse;
import com.lucky.smartadplatform.application.rest.model.ImageDto;
import com.lucky.smartadplatform.application.rest.model.ItemDto;
import com.lucky.smartadplatform.application.rest.model.StatusResponse;
import com.lucky.smartadplatform.application.rest.model.StatusResponse.StatusCode;
import com.lucky.smartadplatform.domain.Item;
import com.lucky.smartadplatform.domain.service.ItemService;
import com.lucky.smartadplatform.infrastructure.security.service.AuthUtil;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/items")
public class ItemController {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ItemService itemService;

    @GetMapping
    public ResponseEntity<GenericResponse<List<ItemDto>>> getItems(
            @RequestParam(name = "categoryId", required = false) Long categoryId) {
        var response = new GenericResponse<List<ItemDto>>();

        List<Item> items = itemService.getItems(categoryId);
        List<ItemDto> dtoItems = items.stream().map(item -> modelMapper.map(item, ItemDto.class))
                .collect(Collectors.toList());

        response.setData(dtoItems);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/filtered")
    public ResponseEntity<GenericResponse<List<ItemDto>>> getFilteredItems(
            @RequestParam(name = "categoryId", required = false) Long categoryId,
            @RequestParam(name = "fromPrice", required = false) BigDecimal fromPrice,
            @RequestParam(name = "toPrice", required = false) BigDecimal toPrice) {
        var response = new GenericResponse<List<ItemDto>>();

        List<Item> items = itemService.getFilteredItems(categoryId, fromPrice, toPrice);
        List<ItemDto> dtoItems = items.stream().map(item -> modelMapper.map(item, ItemDto.class))
                .collect(Collectors.toList());

        response.setData(dtoItems);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Secured("ROLE_USER")
    @GetMapping("/owned")
    public ResponseEntity<GenericResponse<List<ItemDto>>> getOwnedItems(
            @RequestParam(name = "categoryId", required = false) Long categoryId,
            @RequestParam(name = "fromPrice", required = false) BigDecimal fromPrice,
            @RequestParam(name = "toPrice", required = false) BigDecimal toPrice) {
        var response = new GenericResponse<List<ItemDto>>();

        String currentUser = AuthUtil.getCurrentUsername();
        List<Item> items = itemService.getOwnedItems(categoryId, currentUser, fromPrice, toPrice);
        List<ItemDto> dtoItems = items.stream().map(item -> modelMapper.map(item, ItemDto.class))
                .collect(Collectors.toList());

        response.setData(dtoItems);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GenericResponse<ItemDto>> getItem(@PathVariable("id") Long id) {
        var response = new GenericResponse<ItemDto>();

        Item item = itemService.getItem(id);
        ItemDto itemDto = modelMapper.map(item, ItemDto.class);

        response.setData(itemDto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping
    public ResponseEntity<StatusResponse> createItem(@RequestBody ItemDto itemDto) {
        var response = new StatusResponse();

        String username = AuthUtil.getCurrentUsername();
        List<Long> imageIds = itemDto.getImages().stream().map(ImageDto::getId).collect(Collectors.toList());
        try {
            itemService.createItem(itemDto.getName(), itemDto.getPrice(), itemDto.getDescription(),
                    itemDto.getContactNumber(),
                    itemDto.getCategory(), username,
                    imageIds);
        } catch (NullPointerException | IOException e) {
            response.setMessage(e.getMessage());
            response.setStatus(StatusCode.FAILED);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        response.setMessage("Item created successfully.");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Secured("ROLE_USER")
    @PutMapping("/{id}")
    public ResponseEntity<StatusResponse> updateItem(@PathVariable("id") Long id, @RequestBody ItemDto itemDto) {
        var response = new StatusResponse();

        String username = AuthUtil.getCurrentUsername();
        List<Long> imageIds = itemDto.getImages().stream().map(ImageDto::getId).collect(Collectors.toList());
        try {
            itemService.updateItem(id, imageIds, itemDto, username);
        } catch (IOException e) {
            response.setMessage(e.getMessage());
            response.setStatus(StatusCode.FAILED);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        response.setMessage("Item updated successfully.");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Secured("ROLE_USER")
    @DeleteMapping("/{id}")
    public ResponseEntity<StatusResponse> deleteItem(@PathVariable("id") Long id) {
        var response = new StatusResponse();

        String username = AuthUtil.getCurrentUsername();
        itemService.deleteItem(id, username);

        response.setMessage("Item deleted.");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
