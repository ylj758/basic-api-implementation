package com.thoughtworks.rslist.api.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.thoughtworks.rslist.dto.RsEvent;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class RsController {
  private List<RsEvent> rsList = initRsList();

  private List<RsEvent> initRsList() {
    List<RsEvent> tempRsList = new ArrayList<>();
    tempRsList.add(new RsEvent("第一条事件","无分类"));
    tempRsList.add(new RsEvent("第二条事件","无分类"));
    tempRsList.add(new RsEvent("第三条事件","无分类"));
    return tempRsList;
  }

  @GetMapping("/rs/{index}")
  public RsEvent getRsEventByIndex(@PathVariable int index){
    return rsList.get(index-1);
  }

  @GetMapping("/rs/list")
  public List<RsEvent> getRsEventByRange(@RequestParam(required = false) Integer start,
                              @RequestParam(required = false) Integer end) throws JsonProcessingException {
    if(start == null || end == null){
      return rsList;
    }
    return rsList.subList(start-1,end);
  }

  @PostMapping("/rs/event")
  public void addRsEvent(@RequestBody RsEvent rsEvent){
    rsList.add(rsEvent);
  }

  @PutMapping("/rs/update")
  public void updateRsEvent(@RequestParam Integer id,
                            @RequestParam(required = false) String eventName,
                            @RequestParam(required = false) String keyword){
    RsEvent rsEvent = rsList.get(id-1);
    if(eventName != null)
      rsEvent.setEventName(eventName);
    if(keyword != null)
      rsEvent.setKeyword(keyword);
    System.out.println(rsList.toString());
  }

  @RequestMapping(value = "/rs/delete",method = RequestMethod.DELETE)
  public void deleteRsEvent(@RequestParam int id){
    rsList.remove(id-1);
    System.out.println(rsList.toString());
  }
}

