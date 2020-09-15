package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.dto.RsEvent;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
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
  public RsEvent getRsEvent(@PathVariable int index){
    return rsList.get(index-1);
  }

  @GetMapping("/rs/list")
  public String getAllRsEvent(@RequestParam(required = false) Integer start,
                              @RequestParam(required = false) Integer end){
    if(start == null || end == null){
      return rsList.toString();
    }
    return rsList.subList(start-1,end).toString();
  }

  @PostMapping("/rs/event")
  public void getAllRsEvent(@RequestBody RsEvent rsEvent){
    rsList.add(rsEvent);
  }
}
