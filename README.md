### 作业描述

* 去掉RsService上的@Service注解
通过使用@Bean这种方式进行spring bean的定义和注入
reference: https://docs.spring.io/spring-javaconfig/docs/1.0.0.M4/reference/html/ch02s02.html

* 阅读：https://docs.spring.io/spring-boot/docs/2.0.x/reference/html/using-boot-spring-beans-and-dependency-injection.html
修改所有的service和controller 将依赖注入的方式改为通过构造函数注入（而非直接在字段上添加@Autowired）

------------------------

### 作业描述
#### 实现如下接口
* 查询投票记录接口：
    参数传入起止时间，查询在该时间范围内的所有投票记录，写测试验证
* 这是最后一堂JPA课，把前面所有没做完的作业包括课上demo的范例都补上！

-----------------
### 作业描述

#### 实现或修改如下接口
* 修改添加热搜事件接口：参照demo，将添加RsEvent持久化到数据库中
    ```
    {
        “eventName”: “热搜事件名”,
         “keyword”: “关键字”,
         “userId”: “user_id”
    }
  ```
  其中user需要是已注册用户，否则添加失败返回400
  

* 修改删除用户接口：参照demo，删除用户时，需要同时删除该用户所创建的热搜事件(使用JPA提供的mapping注解@ManyToOne @OneToMany)
* 添加更新接口
   ```
    request: patch /rs/{rsEventId}
    requestBody: {
                    “eventName”: “新的热搜事件名”,
                     “keyword”: “新的关键字”,
                     “userId”: “user_id”
                }
   ```
  接口要求：当userId和rsEventId所关联的User匹配时，更新rsEvent信息
          当userId和rsEventId所关联的User不匹配时，返回400
          userId为必传字段
          当只传了eventName没传keyword时只更新eventName
          当只传了keyword没传eventName时只更新keyword
          
* 添加投票接口
    ```
    request: post /rs/vote/{rsEventId}
    request body: {
                    voteNum: 5,
                    userId: 1,
                    voteTime: "current time"
                  }  
    接口要求：如果用户剩的票数大于等于voteNum，则能成功给rsEventId对应的热搜事件投票
            如果用户剩的票数小于voteNum,则投票失败，返回400
            考虑到以后需要查询投票记录的需求（根据userId查询他投过票的所有热搜事件，票数和投票时间，根据rsEventId查询所有给他投过票的用户，票数和投票时间），
            创建一个Vote表是一个明智的选择
            目前不用考虑给热搜事件列表排序的问题
  
    ```
* 修改其余所有接口：所有读取操作都改为从数据库中读取数据（包括重构测试）
  注意：需要修改热搜事件返回的数据结构，使其返回热搜事件id和获得的票数:
    ```
        {
            eventName: "event name",
            keyword: "keyword",
            id: "id",
            voteNum: 10
        }
    ```

* 写测试！！！
------------------

### 作业描述

#### 实现如下接口
* 注册用户：参照demo，将注册用户持久化到数据库中(docker容器内启动的mysql数据库，并非内存数据库)
* 获取用户：新增获取用户接口，传递id返回对应id的用户数据
* 删除用户：新增删除用户接口，传递id从数据库中删除对应id用户数据

* 写测试！！！

<span style="color: red"> 注意：最终需要将改动合并到master分支 </span> 