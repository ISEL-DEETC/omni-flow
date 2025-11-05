package costaber.com.github.omniflow.renderer

import costaber.com.github.omniflow.model.Node

/**
 * Interface that represents a HTML node
 * from the template that is subject to render
 *
 * @param <T> the concrete node type
</T> */
interface NodeRenderer<T> {
    /**
     * The element being rendered
     *
     * @return the element being rendered
     */
    val element: Node

    /**
     * Start rendering the element in
     * the given rendering context
     *
     * @param renderingContext the rendering context
     */
    fun beginRender(renderingContext: RenderingContext): T

    /**
     * Finish rendering the element in
     * the given rendering context
     *
     * @param renderingContext the rendering context
     */
    fun endRender(renderingContext: RenderingContext): T
}