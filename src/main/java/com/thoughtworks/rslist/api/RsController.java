package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.dto.RsEvent;
import org.aspectj.apache.bcel.classfile.Module;
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
  public RsEvent getRsEventByIndex(@PathVariable int index){
    return rsList.get(index-1);
  }

  @GetMapping("/rs/list")
  public String getRsEventByRange(@RequestParam(required = false) Integer start,
                              @RequestParam(required = false) Integer end){
    if(start == null || end == null){
      return rsList.toString();
    }
    return rsList.subList(start-1,end).toString();
  }

  @PostMapping("/rs/event")
  public void addRsEvent(@RequestBody RsEvent rsEvent){
    rsList.add(rsEvent);
  }

  @PutMapping("/rs/update")
  public void updateRsEvent(@RequestBody Integer index,
                            @RequestBody(required = false) String eventName,
                            @RequestBody(required = false) String keyword){
    RsEvent rsEvent = rsList.get(index-1);
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
