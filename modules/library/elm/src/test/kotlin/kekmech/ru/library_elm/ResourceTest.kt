package kekmech.ru.library_elm

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class ResourceTest {

    @Test
    fun `isLoading property works correctly`() {
        assertEquals(
            expected = true,
            actual = Mocks.loadingResource.isLoading,
        )
        assertEquals(
            expected = false,
            actual = Mocks.loadingResource.isData,
        )
        assertEquals(
            expected = false,
            actual = Mocks.loadingResource.isError,
        )
    }

    @Test
    fun `isError property works correctly`() {
        assertEquals(
            expected = false,
            actual = Mocks.errorResource.isLoading,
        )
        assertEquals(
            expected = false,
            actual = Mocks.errorResource.isData,
        )
        assertEquals(
            expected = true,
            actual = Mocks.errorResource.isError,
        )
    }

    @Test
    fun `isData property works correctly`() {
        assertEquals(
            expected = false,
            actual = Mocks.dataResource.isLoading,
        )
        assertEquals(
            expected = true,
            actual = Mocks.dataResource.isData,
        )
        assertEquals(
            expected = false,
            actual = Mocks.dataResource.isError,
        )
    }

    @Test
    fun `toResource function works correctly`() {
        assert(Error("Something went wrong").toResource<Error>() is Resource.Error)
        assertEquals(
            expected = Mocks.dataResource,
            actual = "Hello world".toResource(),
        )
    }

    @Test
    fun `toLoadingIfError function works correctly`() {
        assertEquals(
            expected = Mocks.loadingResource,
            actual = Mocks.errorResource.toLoadingIfError(),
        )
        assertEquals(
            expected = Mocks.dataResource,
            actual = Mocks.dataResource.toLoadingIfError(),
        )
    }

    @Test
    fun `merge function works correctly (loading + error + data)`() {
        val listOfLoadingErrorData =
            listOf(
                Mocks.loadingResource,
                Mocks.errorResource,
                Mocks.dataResource,
            )
        assertEquals(
            expected = Mocks.errorResource,
            actual = listOfLoadingErrorData.merge(),
        )
    }

    @Test
    fun `merge function works correctly (loading + data)`() {
        val listOfLoadingErrorData =
            listOf(
                Mocks.loadingResource,
                Mocks.dataResource,
            )
        assertEquals(
            expected = Mocks.loadingResource,
            actual = listOfLoadingErrorData.merge(),
        )
    }

    @Test
    fun `merge function works correctly (error + data)`() {
        val listOfLoadingErrorData =
            listOf(
                Mocks.errorResource,
                Mocks.dataResource,
            )
        assertEquals(
            expected = Mocks.errorResource,
            actual = listOfLoadingErrorData.merge(),
        )
    }

    @Test
    fun `merge function works correctly (data + data)`() {
        val listOfData =
            listOf(
                Mocks.dataResource.copy(value = "Data 1"),
                Mocks.dataResource.copy(value = "Data 2"),
            )
        assertEquals(
            expected = Resource.Data(listOf("Data 1", "Data 2")),
            actual = listOfData.merge(),
        )
    }

    object Mocks {

        val loadingResource = Resource.Loading
        val errorResource = Resource.Error(Error("Something went wrong"))
        val dataResource = Resource.Data("Hello world")
    }
}
