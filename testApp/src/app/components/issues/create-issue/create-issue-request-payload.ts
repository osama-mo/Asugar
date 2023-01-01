export interface CreateIssueRequestPayload {
    title : String | null,
    description : String | null,
    epicId: String | null,
    sprint: String | null,
    manHour: number | null,
    assignedTo: String | null
    issueType: String| null
}
