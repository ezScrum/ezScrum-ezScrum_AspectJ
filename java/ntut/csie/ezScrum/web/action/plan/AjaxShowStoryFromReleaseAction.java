package ntut.csie.ezScrum.web.action.plan;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ntut.csie.ezScrum.iteration.core.IReleasePlanDesc;
import ntut.csie.ezScrum.iteration.core.IStory;
import ntut.csie.ezScrum.pic.core.IUserSession;
import ntut.csie.ezScrum.web.action.PermissionAction;
import ntut.csie.ezScrum.web.helper.ProductBacklogHelper;
import ntut.csie.ezScrum.web.helper.ReleasePlanHelper;
import ntut.csie.ezScrum.web.support.SessionManager;
import ntut.csie.jcis.resource.core.IProject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

public class AjaxShowStoryFromReleaseAction extends PermissionAction {
	private static Log log = LogFactory.getLog(AjaxShowStoryFromReleaseAction.class);
	
	@Override
	public boolean isValidAction() {
		return super.getScrumRole().getAccessReleasePlan();
	}

	@Override
	public boolean isXML() {
		// XML
		return true;
	}
	
	@Override
	public StringBuilder getResponse(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		log.info(" Show Story From Release. ");
		
		// get session info
		IProject project = (IProject) SessionManager.getProject(request);
		IUserSession session = (IUserSession) request.getSession().getAttribute("UserSession");
		ReleasePlanHelper planHelper = new ReleasePlanHelper(project);
		ProductBacklogHelper PBHelper = new ProductBacklogHelper(session, project);
		
		// get parameter info
		String releaseId = request.getParameter("Rid");
		
		// 取得 ReleasePlan
		IReleasePlanDesc plan = planHelper.getReleasePlan(releaseId);
		
		IStory[] storyList = PBHelper.getStoriesByRelease(plan);
		return planHelper.showStoryFromReleae(project, releaseId, storyList);
	}
}
