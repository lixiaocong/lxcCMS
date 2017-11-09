export class PageDataHandler {

    static successResponseFilter(data) {
        return data.result === 'success';
    }
}