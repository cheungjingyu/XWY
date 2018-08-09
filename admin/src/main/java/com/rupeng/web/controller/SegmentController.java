package com.rupeng.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.rupeng.pojo.Segment;
import com.rupeng.service.SegmentService;
import com.rupeng.util.AjaxResult;
import com.rupeng.util.CommonUtils;

@Controller
@RequestMapping("/segment")
public class SegmentController {

	@Autowired
	private SegmentService segmentService;
	/**
	 * 根据篇章id，获取该篇章下的所有的段落
	 * @param chapterId
	 * @return
	 */
	@RequestMapping("/list.do")
	public ModelAndView list(Long chapterId){
		Segment segment = new Segment();
		segment.setChapterId(chapterId);
		List<Segment> segmentList = segmentService.selectList(segment, "seqNum asc");
		
		ModelAndView modelAndView= new ModelAndView("segment/list");
		modelAndView.addObject("segmentList", segmentList);
		modelAndView.addObject("chapterId", chapterId);
		return modelAndView;
	}
	/**
	 * 获得一个添加段落的页面
	 * @param chapterId
	 * @return
	 */
	@RequestMapping(value="/add.do",method=RequestMethod.GET)
	public ModelAndView addPage(Long chapterId){
		return new ModelAndView("segment/add","chapterId",chapterId);
	}
	@RequestMapping(value="/add.do",method=RequestMethod.POST)
	public @ResponseBody AjaxResult addSubmit(Long chapterId,String name,String description,Integer seqNum,String videoCode){
		//非空判断
		if(CommonUtils.isEmpty(name) || CommonUtils.isEmpty(description) || CommonUtils.isEmpty(videoCode) || seqNum==null){
			return AjaxResult.errorInstance("段落名称或者描述或者视频代码或者序号不能为空");
		}
		//name 的唯一性
		Segment segment =new  Segment();
		segment.setName(name);
		if(segmentService.isExisted(segment)){
			return AjaxResult.errorInstance("段落名称已存在");
		}
		//执行添加操作
		segment.setChapterId(chapterId);
		segment.setDescription(description);
		segment.setSeqNum(seqNum);
		segment.setVideoCode(videoCode);
		segmentService.insert(segment);
		return AjaxResult.successInstance("添加成功");
	}
	@RequestMapping(value="/update.do",method=RequestMethod.GET)
	public ModelAndView update(Long id){
		return new ModelAndView("segment/update","segment",segmentService.selectOne(id));
	}
	@RequestMapping(value="/update.do",method=RequestMethod.POST)
	public @ResponseBody AjaxResult updateSubmit(Long id,String name,String description,Integer seqNum,String videoCode){
		//非空判断
		if(CommonUtils.isEmpty(name) || CommonUtils.isEmpty(description) || CommonUtils.isEmpty(videoCode) || seqNum==null){
			return AjaxResult.errorInstance("段落名称或者描述或者视频代码或者序号不能为空");
		}
		//name 的唯一性
		Segment segment = new Segment();
		segment.setName(name);
		segment=segmentService.selectOne(segment);
		if(segment!=null && segment.getId()!=id){
			return AjaxResult.errorInstance("段落名称已存在");
		}
		//执行更新操作 
		segment.setDescription(description);
		segment.setName(name);
		segment.setSeqNum(seqNum);
		segment.setVideoCode(videoCode);
		segmentService.update(segment);
		return AjaxResult.successInstance("修改成功");
	}
	@RequestMapping("/delete.do")
	public @ResponseBody AjaxResult delete(Long id){
		segmentService.delete(id);
		return AjaxResult.successInstance("删除成功");
	}
}
