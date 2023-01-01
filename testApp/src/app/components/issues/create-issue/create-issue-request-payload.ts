export interface CreateIssueRequestPayload {
    title : String | null,
    description : String | null,
    epicId: String | null,
    sprint: String | null,
    manHour: number | null,
    issueType: String| null
}
