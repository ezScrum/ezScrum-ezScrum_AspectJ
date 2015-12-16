package ntut.csie.ezScrum.web.action.unplan;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ntut.csie.ezScrum.web.dataObject.ProjectObject;
import ntut.csie.ezScrum.web.dataObject.UnplanObject;
import ntut.csie.ezScrum.web.helper.UnplanItemHelper;
import ntut.csie.ezScrum.web.support.SessionManager;
import ntut.csie.ezScrum.web.support.TranslateSpecialChar;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class ShowEditUnplanItemAction extends Action {
	
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {

		// get session info
		ProjectObject project = SessionManager.getProjectObject(request);
		long unplanId = Long.parseLong(request.getParameter("issueID"));
		
		// Get unplan item
		UnplanItemHelper unplanHelper = new UnplanItemHelper(project);
		UnplanObject unplan = unplanHelper.getUnplan(unplanId);
		
		// return result of unplan item in XML
		StringBuilder result = new StringBuilder();
		result.append("<EditUnplannedItem><UnplannedItem>")
		  .append("<Id>").append(unplan.getId()).append("</Id>")
		  .append("<Link></Link>")
		  .append("<Name>").append(TranslateSpecialChar.TranslateXMLChar(unplan.getName())).append("</Name>")
		  .append("<SprintID>").append(unplan.getSprintId()).append("</SprintID>")
		  .append("<Estimate>").append(unplan.getEstimate()).append("</Estimate>")
		  .append("<Status>").append(unplan.getStatusString()).append("</Status>")
		  .append("<ActualHour>").append(unplan.getActual()).append("</ActualHour>")
		  .append("<Handler>").append(unplan.getHandlerName()).append("</Handler>")
		  .append("<Partners>").append(TranslateSpecialChar.TranslateXMLChar(unplan.getPartnersUsername())).append("</Partners>")
		  .append("<Notes>").append(TranslateSpecialChar.TranslateXMLChar(unplan.getNotes())).append("</Notes>")
		  .append("</UnplannedItem></EditUnplannedItem>");
		
		try {
			response.setContentType("text/xml; charset=utf-8");
			response.getWriter().write(result.toString());
			response.getWriter().close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}
}
