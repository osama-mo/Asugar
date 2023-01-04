export interface CreateEpicRequestPayload {
    title : String | null,
    description : String | null,
    manHour: number | null,
    assignedTo: String,
    due: String | null,
}
