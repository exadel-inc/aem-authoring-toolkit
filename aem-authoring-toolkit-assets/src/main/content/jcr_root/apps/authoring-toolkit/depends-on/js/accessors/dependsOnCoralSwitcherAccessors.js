/**
 * @author Alexey Stsefanovich (ala'n), Yana Bernatskaya (YanaBr)
 * @version 2.2.4
 *
 * Coral 3 Single Radio Button/Checkbox/Switch and Radio Group accessors
 * */
(function ($, ns) {
    const SWITCH_INPUT_SELECTOR = '.coral3-Switch-input';

    // Coral 3 Single Radio Button/Checkbox/Switch
    ns.ElementAccessors.registerAccessor({
        selector: 'input[type="radio"],input[type="checkbox"],coral-radio,coral-checkbox,coral-switch',
        preferableType: 'boolean',
        get: function ($el) {
            return $el[0].checked;
        },
        set: function ($el, val, notify) {
            $el.each(function () {
                this.checked = val;
            });
            notify && $el.trigger('change');
        },
        visibility: function ($el, state) {
            $el.find(SWITCH_INPUT_SELECTOR).attr('type', state ? 'text' : 'hidden');
            ns.ElementAccessors.DEFAULT_ACCESSOR.visibility($el, state);
        }
    });

    // Coral 3 Radio Group
    ns.ElementAccessors.registerAccessor({
        selector: '.coral-RadioGroup',
        preferableType: 'string',
        get: function ($el) {
            return $el.find('coral-radio[checked]').val() || '';
        },
        set: function ($el, val, notify) {
            $el.find('coral-radio').each(function () {
                this.checked = val === this.value;
            });
            notify && $el.trigger('change');
        }
    });
})(Granite.$, Granite.DependsOnPlugin = (Granite.DependsOnPlugin || {}));