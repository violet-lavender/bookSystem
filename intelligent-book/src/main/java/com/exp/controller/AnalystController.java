package com.exp.controller;

import com.exp.anno.RequiresRole;
import com.exp.pojo.Role;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequestMapping("/analysts")
@RestController
@RequiresRole({Role.ANALYST}) // 权限管理
public class AnalystController {
}
