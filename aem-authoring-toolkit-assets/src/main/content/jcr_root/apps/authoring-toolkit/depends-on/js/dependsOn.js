/**
 * @author Alexey Stsefanovich (ala'n)
 * @version 2.2.2
 *
 * DependsOn plugin entry point
 *
 * Initialize QueryObserver and ElementReferences
 * */
(function ($, ns) {
    'use strict';

    const $window = $(window);
    const $document = $(document);

    /**
     * Depends On entry point
     * Initialize DependsOn for container
     * @param {HTMLElement} container
     * @param {function} [callback]
     * */
    ns.initialize = function (container, callback) {
        // Wait core-components:ready for new content and then plan initialization task
        Coral.commons.ready(container, () => setTimeout(() => {
            // GC and update simple references first
            ns.ElementReferenceRegistry.actualize();
            $('[data-dependsonref]', container).each((i, el) => ns.ElementReferenceRegistry.registerElement($(el)));
            // GC for group references
            ns.GroupReferenceRegistry.actualize();
            // Observers initialization
            $('[data-dependson]', container).each((i, el) => ns.QueryObserver.init($(el)));
            // Execute callback if provided
            (typeof callback === 'function') && callback();
        }));
    };

    // Track new component initialization
    $document.off('foundation-contentloaded.dependsOn')
        .on('foundation-contentloaded.dependsOn', (e) => ns.initialize(e && e.target || document));

    // Track reference field changes
    $document
        .off('change.dependsOn').on('change.dependsOn', '[data-dependsonref]', ns.ElementReferenceRegistry.handleChange)
        .off('selected.dependsOn').on('selected.dependsOn', '[data-dependsonref]', ns.ElementReferenceRegistry.handleChange);

    // Track collection change to update dynamic references
    $document
        .off('coral-collection:remove.dependsOn coral-collection:add.dependsOn').on('coral-collection:remove.dependsOn coral-collection:add.dependsOn', 'coral-multifield', (e) => {
            // We should actualize references on coral-collection:remove and coral-collection:add too.
            ns.ElementReferenceRegistry.handleChange(e);
            ns.ElementReferenceRegistry.actualize();
            ns.GroupReferenceRegistry.actualize();
        });

    // ----
    // Validation control: exclude element and its child from validation in hidden state.
    $window.adaptTo('foundation-registry').register('foundation.validation.selector', {
        exclusion: '[data-dependson][hidden], [data-dependson-controllable][hidden]'
    });
    $window.adaptTo('foundation-registry').register('foundation.validation.selector', {
        exclusion: '[data-dependson][hidden] *, [data-dependson-controllable][hidden] *'
    });
})(Granite.$, Granite.DependsOnPlugin = (Granite.DependsOnPlugin || {}));